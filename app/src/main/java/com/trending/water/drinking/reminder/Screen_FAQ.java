package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import android.view.LayoutInflater;

import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.custom.AnimationUtils;
import com.trending.water.drinking.reminder.databinding.RowItemFaqBinding;
import com.trending.water.drinking.reminder.databinding.ScreenFaqBinding;
import com.trending.water.drinking.reminder.model.FAQModel;

import java.util.ArrayList;
import java.util.List;

public class Screen_FAQ extends MasterBaseActivity<ScreenFaqBinding> {

    @Override
    protected ScreenFaqBinding inflateBinding(LayoutInflater inflater) {
        return ScreenFaqBinding.inflate(inflater);
    }

    private final List<FAQModel> faqList = new ArrayList<>();
    private final List<LinearLayout> answerViewList = new ArrayList<>();
    private final List<ImageView> expandIconList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        binding.include1.lblToolbarTitle.setText(stringHelper.getString(R.string.str_faqs));
        binding.include1.leftIconBlock.setOnClickListener(v -> finish());
        binding.include1.rightIconBlock.setVisibility(View.GONE);

        initializeFAQData();
        renderFAQRows();
    }

    private void initializeFAQData() {
        faqList.clear();
        addFAQ(R.string.faq_question_1, R.string.faq_answer_1);
        addFAQ(R.string.faq_question_2, R.string.faq_answer_2);
        addFAQ(R.string.faq_question_3, R.string.faq_answer_3);
        addFAQ(R.string.faq_question_12, R.string.faq_answer_12);
        addFAQ(R.string.faq_question_13, R.string.faq_answer_13);
        addFAQ(R.string.faq_question_4, R.string.faq_answer_4);
        addFAQ(R.string.faq_question_11, R.string.faq_answer_11);
        addFAQ(R.string.faq_question_5, R.string.faq_answer_5);
        addFAQ(R.string.faq_question_6, R.string.faq_answer_6);
        addFAQ(R.string.faq_question_7, R.string.faq_answer_7);
        addFAQ(R.string.faq_question_8, R.string.faq_answer_8);
        addFAQ(R.string.faq_question_9, R.string.faq_answer_9);
    }

    private void addFAQ(int qResId, int aResId) {
        FAQModel model = new FAQModel();
        model.setQuestion(stringHelper.getString(qResId));
        model.setAnswer(stringHelper.getString(aResId));
        faqList.add(model);
    }

    private void renderFAQRows() {
        binding.faqBlock.removeAllViews();
        answerViewList.clear();
        expandIconList.clear();

        for (int i = 0; i < faqList.size(); i++) {
            final int pos = i;
            FAQModel faq = faqList.get(i);

            RowItemFaqBinding rowBinding = RowItemFaqBinding.inflate(LayoutInflater.from(mContext), binding.faqBlock, false);

            answerViewList.add(rowBinding.answerBlock);
            expandIconList.add(rowBinding.imgFaq);

            rowBinding.lblQuestion.setText(faq.getQuestion());
            rowBinding.lblAnswer.setText(faq.getAnswer());

            rowBinding.questionBlock.setOnClickListener(v -> {
                if (rowBinding.answerBlock.getVisibility() == View.GONE) {
                    collapseAllExcept(pos);
                    rowBinding.imgFaq.setImageResource(R.drawable.ic_faq_minus);
                    AnimationUtils.expand(rowBinding.answerBlock);
                } else {
                    rowBinding.imgFaq.setImageResource(R.drawable.ic_faq_plus);
                    AnimationUtils.collapse(rowBinding.answerBlock);
                }
            });

            binding.faqBlock.addView(rowBinding.getRoot());
        }
    }

    private void collapseAllExcept(int pos) {
        for (int i = 0; i < answerViewList.size(); i++) {
            if (i != pos && answerViewList.get(i).getVisibility() == View.VISIBLE) {
                expandIconList.get(i).setImageResource(R.drawable.ic_faq_plus);
                AnimationUtils.collapse(answerViewList.get(i));
            }
        }
    }
}
