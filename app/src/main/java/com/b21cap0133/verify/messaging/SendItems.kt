package com.b21cap0133.verify.messaging

import android.text.TextUtils
import android.view.View
import com.b21cap0133.verify.R
import com.b21cap0133.verify.databinding.SendItemBinding
import com.b21cap0133.verify.model.Message
import com.xwray.groupie.viewbinding.BindableItem

class SendItems(private val message: Message): BindableItem<SendItemBinding>() {
    override fun initializeViewBinding(view: View): SendItemBinding {
        return SendItemBinding.bind(view)
    }

    override fun bind(viewBinding: SendItemBinding, position: Int) {
        viewBinding.sendText.text = message.content
        if (viewBinding.sendText.ellipsize == null){
            viewBinding.sendText.maxLines = 4
            viewBinding.sendText.ellipsize = TextUtils.TruncateAt.END
        }else{
            viewBinding.sendText.maxLines = Integer.MAX_VALUE
            viewBinding.sendText.ellipsize = null
        }
    }

    override fun getLayout(): Int {
        return R.layout.send_item
    }
}