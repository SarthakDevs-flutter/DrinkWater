package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.custom.AnimationUtils;
import com.trending.water.drinking.reminder.model.FAQModel;

import java.util.ArrayList;
import java.util.List;

public class Screen_FAQ extends MasterBaseActivity {

    private LinearLayout faqContainer;
    private AppCompatTextView lblToolbarTitle;
    private LinearLayout leftIconBlock;
    private LinearLayout rightIconBlock;

    private final List<FAQModel> faqList = new ArrayList<>();
    private final List<LinearLayout> answerViewList = new ArrayList<>();
    private final List<ImageView> expandIconList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_faq);
        
        findViewByIds();
        initView();
    }

    private void findViewByIds() {
        rightIconBlock = findViewById(R.id.right_icon_block);
        leftIconBlock = findViewById(R.id.left_icon_block);
        lblToolbarTitle = findViewById(R.id.lbl_toolbar_title);
        faqContainer = findViewById(R.id.faq_block);
    }

    private void initView() {
        lblToolbarTitle.setText(stringHelper.getString(R.string.str_faqs));
        leftIconBlock.setOnClickListener(v -> finish());
        rightIconBlock.setVisibility(View.GONE);

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
        faqContainer.removeAllViews();
        answerViewList.clear();
        expandIconList.clear();

        for (int i = 0; i < faqList.size(); i++) {
            final int pos = i;
            FAQModel faq = faqList.get(i);
            
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.row_item_faq, faqContainer, false);
            LinearLayout answerBlock = itemView.findViewById(R.id.answer_block);
            ImageView imgFaq = itemView.findViewById(R.id.img_faq);
            AppCompatTextView lblQuestion = itemView.findViewById(R.id.lbl_question);
            AppCompatTextView lblAnswer = itemView.findViewById(R.id.lbl_answer);
            LinearLayout questionBlock = itemView.findViewById(R.id.question_block);

            answerViewList.add(answerBlock);
            expandIconList.add(imgFaq);

            lblQuestion.setText(faq.getQuestion());
            lblAnswer.setText(faq.getAnswer());

            questionBlock.setOnClickListener(v -> {
                if (answerBlock.getVisibility() == View.GONE) {
                    collapseAllExcept(pos);
                    imgFaq.setImageResource(R.drawable.ic_faq_minus);
                    AnimationUtils.expand(answerBlock);
                } else {
                    imgFaq.setImageResource(R.drawable.ic_faq_plus);
                    AnimationUtils.collapse(answerBlock);
                }
            });

            faqContainer.addView(itemView);
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
