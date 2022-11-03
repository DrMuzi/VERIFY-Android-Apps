package com.b21cap0133.verify.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.b21cap0133.verify.MainActivity
import com.b21cap0133.verify.R
import com.b21cap0133.verify.databinding.FragmentCheckHoaxBinding
import com.b21cap0133.verify.domain.Request
import com.b21cap0133.verify.messaging.HintItem
import com.b21cap0133.verify.messaging.ReceiveItems
import com.b21cap0133.verify.messaging.SendItems
import com.b21cap0133.verify.model.Message
import com.b21cap0133.verify.ui.checkhoax.CheckHoaxViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import org.koin.androidx.viewmodel.ext.android.viewModel

class CheckHoaxFragment : Fragment() {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private var binding : FragmentCheckHoaxBinding? = null
    private val viewBind get() = binding as FragmentCheckHoaxBinding
    private lateinit var recyclerView: RecyclerView
    private val checkViewModel: CheckHoaxViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val parent: MainActivity = activity as MainActivity
        parent.hideUpButton()
        recyclerView = viewBind.chatRecyclerView
        recyclerView.adapter = adapter
        initialMessage()
        viewBind.buttonSend.setOnClickListener {
            val userInput = viewBind.textInput.text.toString()
            val message = Message("me", userInput)
            val requestBody = Request(userInput)
            val send = SendItems(message)
            adapter.add(send)
            viewBind.textInput.text.clear()
            recyclerView.scrollToPosition(adapter.itemCount - 1)
            receiveAutoResponse(requestBody)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCheckHoaxBinding.inflate(inflater, container, false)
        return viewBind.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //this is the thing that makes request and receives result
    private fun receiveAutoResponse(user: Request) {
        val data = checkViewModel.getResult(user)
        data.observe(viewLifecycleOwner, {
            val text = if (it.judul != "notfound404"){
                 """
                     ${getString(R.string.underline_tooltip_text)}
                     Menurut prediksi kami berita ini ${it.prediksi.subSequence(0,4)}% hoax
                     Ringkasan: ${it.preview}
                 """.trimIndent()
            }else{
               "Kami tidak menemukan berita tersebut"
            }
            val receive = Message("server", text, it.linkBerita)
            val receiveItem = ReceiveItems(receive)
            adapter.add(receiveItem)
            recyclerView.scrollToPosition(adapter.itemCount - 1)
            //destroy observer once done
            data.removeObservers(viewLifecycleOwner)
        })
        /*GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            val receive = Message("server", "ご覧いただきありがとうございます！この一回きりのパフォーマンスに全身全霊で挑みました。少しでも皆さんの明日を照らす青い光となれたら嬉しいです。(ikura)")
            val receiveItem = ReceiveItems(receive)
            adapter.add(receiveItem)
        }*/
    }

    //hint
    private fun initialMessage(){
        val receive = Message("hint", "Anda dapat memasukkan judul berita ataupun topik yang ingin dicari tau kebenarannya. Tekan chatbox untuk melihat berita selengkapnya")
        val receiveItem = HintItem(receive)
        adapter.add(receiveItem)
    }
}
