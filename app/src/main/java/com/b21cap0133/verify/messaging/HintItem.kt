package com.b21cap0133.verify.messaging

import android.view.View
import com.b21cap0133.verify.R
import com.b21cap0133.verify.databinding.InitialHintBinding
import com.b21cap0133.verify.model.Message
import com.xwray.groupie.viewbinding.BindableItem

class HintItem(private val message: Message): BindableItem<InitialHintBinding>() {
    override fun initializeViewBinding(view: View): InitialHintBinding {
        return InitialHintBinding.bind(view)
    }

    override fun bind(viewBinding: InitialHintBinding, position: Int) {
        viewBinding.sendText.text = message.content
    }

    override fun getLayout(): Int {
        return R.layout.initial_hint
    }
}