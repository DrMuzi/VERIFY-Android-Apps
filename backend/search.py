import json
import nltk
import pandas as pd
import regex as re
import googleapiclient.discovery
from google.api_core.client_options import ClientOptions
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity
nltk.download('punkt')

## to run locally
# from io import StringIO
# from gcloud import storage
# import os
# os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = 'gckey.json'

def read_db():
    ## to auth gcloud bucket access from local machine
    ## don't use if deploy to gcp cuz u don't need auth
    # os.environ['GOOGLE_APPLICATION_CREDENTIALS'] = 'https://storage.googleapis.com/data_csv_bucket/gckey.json'

    ## to take data from gcloud bucket
    # storage_client = storage.Client()
    # bucket = storage_client.get_bucket('data_csv_bucket')
    #
    # blob = bucket.blob('dB_tbh.csv')
    # blob = blob.download_as_string()
    # blob = blob.decode('utf-8')
    #
    # path = StringIO(blob)  # tranform bytes to string here
    path = 'dB_tbh.csv'
    db = pd.read_csv(path)

    judul = db['Judul'].apply(str).values.tolist()
    isi = db['Isi berita'].apply(str).values.tolist()
    url = db['Link'].values.tolist()
    date = db['Tanggal'].values.tolist()

    return judul, isi, url, date

def get_cosine_sim(*strs):
    vectors = [t for t in get_vectors(*strs)]
    return cosine_similarity(vectors)

def get_vectors(*strs):
    text = [t for t in strs]
    vectorizer = CountVectorizer(text)
    vectorizer.fit(text)
    return vectorizer.transform(text).toarray()

def cek_kesamaan(headline_input, pembanding):
    cosine_sim = []
    for p in pembanding:
        cs = get_cosine_sim(headline_input, p).min()
        cosine_sim.append(cs)

    maks = cosine_sim.index(max(cosine_sim))
    top = pembanding[maks]

    return max(cosine_sim), top

def write_json(judul, prediksi, url, date, kategori, penjelasan, preview):
    response_json = {
      'code': 200,
      'judul': judul,
      'prediksi': prediksi,
      'url': url,
      'tanggal': date,
      'kategori': kategori,             # TAMBAHAN
      'penjelasan': penjelasan,         # TAMBAHAN
      'preview': preview                # TAMBAHAN
    }
    with open("result.json", "w") as f:
        json.dump(response_json, f)

    return json.dumps(response_json)

def write_json_notfound():
    to_json = {
        'code': 404,
        'isi': 'Tidak ditemukan berita'
    }
    with open("message.json", "w") as f:
        json.dump(to_json, f)

def filter_content(content):
    content = content.replace('\n', ' ')
    content = content.replace('\u201c', ' ')
    content = content.replace('\u201d', ' ')
    regex = r"(.*)(Kategori:.*)(===== Sumber:.*)(===== Penjelasan:.*)(===== Referensi:.*)"
    matches = re.match(regex, content, re.M | re.I)

    if matches == None:
        kategori = None
        penjelasan = content
    else:
        kategori = matches.group(2).replace('Kategori:', '')
        penjelasan = matches.group(4).replace('===== Penjelasan:', '')

    preview = ' '.join(nltk.tokenize.sent_tokenize(penjelasan)[:2])

    return kategori, penjelasan, preview

def predict_json(input, version=None):
    #Send json data to a deployed model for prediction.

    # Create the ML Engine service object.
    # To authenticate on a local machine, set the environment variable
    # don't use if deploy to gcp cuz u don't need auth

    ## vars
    project = 'capstone-verify' #project-id
    region = 'asia-southeast1' #region where the model deployed
    model = 'verify_tf_model' #model name

    prefix = "{}-ml".format(region) if region else "ml"
    api_endpoint = "https://{}.googleapis.com".format(prefix)
    client_options = ClientOptions(api_endpoint=api_endpoint)
    service = googleapiclient.discovery.build(
        'ml', 'v1', client_options=client_options)
    name = 'projects/{}/models/{}'.format(project, model)

    if version is not None:
        name += '/versions/{}'.format(version)

    response = service.projects().predict(
        name=name,
        body={'instances': [input]}
    ).execute()

    if 'error' in response:
        raise RuntimeError(response['error'])
    result = response['predictions'] #formed in [[value]]
    result = result[0] #unpack the first []
    result = result[0] #unpack the second []
    result *= 100 #return in percentage
    return result #return float only without list

def predict_and_search(headline_input):
    judul, isi, url, date = read_db()
    prediksi = predict_json(headline_input)

    title_similarity_score, selected_title = cek_kesamaan(headline_input, judul)

    # kondisi ketika pencarian tidak dilakukan
    if title_similarity_score > 0:
        idx = judul.index(selected_title)
        selected_title = selected_title.replace('\u201c', ' ')
        selected_title = selected_title.replace('\u201d', ' ')
        kategori, penjelasan, preview = filter_content(isi[idx])
        response = write_json(selected_title, prediksi, url[idx], date[idx], kategori, penjelasan, preview)
        result = 'berita tersedia'

    # kondisi ketika pencarian perlu dilakukan
    else:
        content_similarity_score, selected_content = cek_kesamaan(headline_input, isi)

        # kondisi ketika tidak ditemukan kecocokan sama sekali
        if content_similarity_score == 0:
            write_json_notfound()
            result = 'berita belum tersedia'
        else:
            idx = isi.index(selected_content)
            kategori, penjelasan, preview = filter_content(selected_content)
            response = write_json(judul[idx], prediksi, url[idx], date[idx], kategori, penjelasan, preview)
            result = 'berita tersedia di isi'
    return result, response
