package com.miko.story.presentation.membership.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.InputType
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.miko.story.R
import com.miko.story.databinding.LayoutStoryEditTextBinding
import com.miko.story.utils.gone

@SuppressLint("ClickableViewAccessibility")
class StoryEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mBinding: LayoutStoryEditTextBinding? = null
    private val binding get() = mBinding!!
    private var mIsValid = false
    val isValid get() = mIsValid
    val text get() = binding.edtStory.text?.toString() ?: ""

    private val validations: MutableList<Validation> = mutableListOf()
    private var storyInputType = StoryInputType.EMAIL.value
    private var errorMessage: String? = null
    private var label: String? = null
    private var multiline: Boolean = false
    private var isPasswordShowed = false
    private var showPasswordImage: Drawable? = null

    init {
        mBinding = LayoutStoryEditTextBinding.inflate(LayoutInflater.from(getContext()), this, true)

        val attributes = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.StoryEditText,
            0,
            0
        )

        storyInputType = attributes.getInteger(R.styleable.StoryEditText_storyInputType, 0x00000000)
        label = attributes.getString(R.styleable.StoryEditText_storyLabel)
        multiline = attributes.getBoolean(R.styleable.StoryEditText_storyMultiline, false)

        with(binding) {
            when (storyInputType) {
                StoryInputType.EMAIL.value -> {
                    edtStory.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    addValidation(EmailValidation(context.getString(R.string.error_email_not_matched)))
                }
                StoryInputType.PASSWORD.value -> {
                    edtStory.apply {
                        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        showPasswordImage = ContextCompat.getDrawable(context, R.drawable.ic_eye_opened)
                        setShowPassword()
                    }
                    addValidation(MinLengthValidation(6, context.getString(R.string.error_password_must_be_longer)))
                }
                StoryInputType.NORMAL.value -> {
                    if (multiline) {
                        edtStory.apply {
                            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                            gravity = Gravity.START or Gravity.TOP
                        }
                    } else {
                        edtStory.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                    }
                    tvError.gone()
                }
                else -> throw IllegalStateException("StoryInputType unknown")
            }

            tvLabel.text = label

            if (storyInputType == StoryInputType.PASSWORD.value && showPasswordImage != null) {
                edtStory.setOnTouchListener { v, event ->
                    var isClearButtonClicked = false
                    if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                        val showPasswordButtonEnd = (showPasswordImage!!.intrinsicWidth + v.paddingStart).toFloat()
                        when {
                            event.x < showPasswordButtonEnd -> isClearButtonClicked = true
                        }
                    } else {
                        val showPasswordStart = (width - v.paddingEnd - showPasswordImage!!.intrinsicWidth).toFloat()
                        when {
                            event.x > showPasswordStart -> isClearButtonClicked = true
                        }
                    }
                    if (isClearButtonClicked) {
                        when (event.action) {
                            MotionEvent.ACTION_UP -> {
                                isPasswordShowed = !isPasswordShowed
                                setShowPassword()
                                return@setOnTouchListener true
                            }
                            else -> return@setOnTouchListener false
                        }
                    } else return@setOnTouchListener false
                }
            }

            if (validations.isNotEmpty()) {
                edtStory.addTextChangedListener {
                    validate(it?.toString() ?: "")
                }
            }
        }
    }

    private fun setShowPassword() {
        with(binding) {
            if (isPasswordShowed) {
                edtStory.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
                showPasswordImage = ContextCompat.getDrawable(context, R.drawable.ic_eye_closed)
            } else {
                edtStory.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                showPasswordImage = ContextCompat.getDrawable(context, R.drawable.ic_eye_opened)
            }
            edtStory.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, showPasswordImage, null)
        }
    }

    override fun onDetachedFromWindow() {
        mBinding = null
        super.onDetachedFromWindow()
    }

    private fun validate(text: String) {
        with(binding) {
            for (i in 0 until validations.size) {
                if (!validations[i].validate(text)) {
                    tvError.text = if (text.isNotEmpty()) {
                        validations[i].message.also {
                            errorMessage = it
                        }
                    } else {
                        ""
                    }
                    mIsValid = false
                    break
                } else {
                    if (tvError.text.isNotEmpty()) tvError.text = ""
                    if (i == validations.lastIndex) mIsValid = true
                }
            }
        }
    }

    private fun addValidation(validation: Validation) {
        validations.add(validation)
    }

    private class EmailValidation(message: String = "") : Validation(message) {
        override fun validate(text: String): Boolean {
            return ("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-" +
                    "\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:" +
                    "[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]" +
                    "*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])").toRegex()
                .matches(text)
        }
    }

    private class MinLengthValidation(private val minLength: Int, message: String = "") : Validation(message) {
        override fun validate(text: String): Boolean {
            return text.length >= minLength
        }
    }

    private abstract class Validation(val message: String) {
        abstract fun validate(text: String): Boolean
    }


    enum class StoryInputType(val value: Int) {
        EMAIL(0x00000000),
        PASSWORD(0x00000001),
        NORMAL(0x00000002)
    }
}