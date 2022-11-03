package com.b21cap0133.verify.messaging

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.b21cap0133.verify.R
import com.b21cap0133.verify.databinding.ReceiveItemBinding
import com.b21cap0133.verify.model.Message
import com.xwray.groupie.viewbinding.BindableItem

class ReceiveItems(private val message: Message) : BindableItem<ReceiveItemBinding>() {

    override fun initializeViewBinding(view: View): ReceiveItemBinding {
        return ReceiveItemBinding.bind(view)
    }

    override fun bind(viewBinding: ReceiveItemBinding, position: Int) {
        viewBinding.sendText.text = message.content
        viewBinding.sendText.setOnClickListener {
            if (viewBinding.sendText.ellipsize == null) {
                viewBinding.sendText.maxLines = 5
                viewBinding.sendText.ellipsize = TextUtils.TruncateAt.END
            } else {
                viewBinding.sendText.maxLines = Integer.MAX_VALUE
                viewBinding.sendText.ellipsize = null
            }
        }
        viewBinding.link.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(message.link)
            startActivity(it.context, intent, null)
        }
    }

    override fun getLayout(): Int {
        return R.layout.receive_item
    }
}