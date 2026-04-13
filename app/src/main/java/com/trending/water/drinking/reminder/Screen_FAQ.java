package com.trending.water.drinking.reminder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.trending.water.drinking.reminder.base.MasterBaseActivity;
import com.trending.water.drinking.reminder.custom.AnimationUtils;
import com.trending.water.drinking.reminder.model.FAQModel;

import java.util.ArrayList;
import java.util.List;

public class Screen_FAQ extends MasterBaseActivity {
    List<LinearLayout> answer_block_lst = new ArrayList();
    LinearLayout faq_block;
    List<ImageView> img_faq_lst = new ArrayList();
    AppCompatTextView lbl_toolbar_title;
    LinearLayout left_icon_block;
    List<FAQModel> lst_faq = new ArrayList();
    LinearLayout right_icon_block;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_faq);
        FindViewById();
        Body();
    }

    private void FindViewById() {
        this.right_icon_block = (LinearLayout) findViewById(R.id.right_icon_block);
        this.left_icon_block = (LinearLayout) findViewById(R.id.left_icon_block);
        this.lbl_toolbar_title = (AppCompatTextView) findViewById(R.id.lbl_toolbar_title);
        this.faq_block = (LinearLayout) findViewById(R.id.faq_block);
    }

    private void Body() {
        this.lbl_toolbar_title.setText(this.sh.get_string(R.string.str_faqs));
        this.left_icon_block.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Screen_FAQ.this.finish();
            }
        });
        this.right_icon_block.setVisibility(View.GONE);
        setFAQData();
        loadFAQData();
    }

    public void setFAQData() {
        FAQModel faqModel = new FAQModel();
        faqModel.setQuestion(this.sh.get_string(R.string.faq_question_1));
        faqModel.setAnswer(this.sh.get_string(R.string.faq_answer_1));
        this.lst_faq.add(faqModel);
        FAQModel faqModel2 = new FAQModel();
        faqModel2.setQuestion(this.sh.get_string(R.string.faq_question_2));
        faqModel2.setAnswer(this.sh.get_string(R.string.faq_answer_2));
        this.lst_faq.add(faqModel2);
        FAQModel faqModel3 = new FAQModel();
        faqModel3.setQuestion(this.sh.get_string(R.string.faq_question_3));
        faqModel3.setAnswer(this.sh.get_string(R.string.faq_answer_3));
        this.lst_faq.add(faqModel3);
        FAQModel faqModel4 = new FAQModel();
        faqModel4.setQuestion(this.sh.get_string(R.string.faq_question_12));
        faqModel4.setAnswer(this.sh.get_string(R.string.faq_answer_12));
        this.lst_faq.add(faqModel4);
        FAQModel faqModel5 = new FAQModel();
        faqModel5.setQuestion(this.sh.get_string(R.string.faq_question_13));
        faqModel5.setAnswer(this.sh.get_string(R.string.faq_answer_13));
        this.lst_faq.add(faqModel5);
        FAQModel faqModel6 = new FAQModel();
        faqModel6.setQuestion(this.sh.get_string(R.string.faq_question_4));
        faqModel6.setAnswer(this.sh.get_string(R.string.faq_answer_4));
        this.lst_faq.add(faqModel6);
        FAQModel faqModel7 = new FAQModel();
        faqModel7.setQuestion(this.sh.get_string(R.string.faq_question_11));
        faqModel7.setAnswer(this.sh.get_string(R.string.faq_answer_11));
        this.lst_faq.add(faqModel7);
        FAQModel faqModel8 = new FAQModel();
        faqModel8.setQuestion(this.sh.get_string(R.string.faq_question_5));
        faqModel8.setAnswer(this.sh.get_string(R.string.faq_answer_5));
        this.lst_faq.add(faqModel8);
        FAQModel faqModel9 = new FAQModel();
        faqModel9.setQuestion(this.sh.get_string(R.string.faq_question_6));
        faqModel9.setAnswer(this.sh.get_string(R.string.faq_answer_6));
        this.lst_faq.add(faqModel9);
        FAQModel faqModel10 = new FAQModel();
        faqModel10.setQuestion(this.sh.get_string(R.string.faq_question_7));
        faqModel10.setAnswer(this.sh.get_string(R.string.faq_answer_7));
        this.lst_faq.add(faqModel10);
        FAQModel faqModel11 = new FAQModel();
        faqModel11.setQuestion(this.sh.get_string(R.string.faq_question_8));
        faqModel11.setAnswer(this.sh.get_string(R.string.faq_answer_8));
        this.lst_faq.add(faqModel11);
        FAQModel faqModel12 = new FAQModel();
        faqModel12.setQuestion(this.sh.get_string(R.string.faq_question_9));
        faqModel12.setAnswer(this.sh.get_string(R.string.faq_answer_9));
        this.lst_faq.add(faqModel12);
    }

    public void loadFAQData() {
        this.faq_block.removeAllViews();
        for (int k = 0; k < this.lst_faq.size(); k++) {
            final int pos = k;
            FAQModel rowModel = this.lst_faq.get(k);
            View itemView = LayoutInflater.from(this.mContext).inflate(R.layout.row_item_faq, (ViewGroup) null, false);
            final LinearLayout answer_block = (LinearLayout) itemView.findViewById(R.id.answer_block);
            final ImageView img_faq = (ImageView) itemView.findViewById(R.id.img_faq);
            this.answer_block_lst.add(answer_block);
            this.img_faq_lst.add(img_faq);
            ((AppCompatTextView) itemView.findViewById(R.id.lbl_question)).setText(rowModel.getQuestion());
            ((AppCompatTextView) itemView.findViewById(R.id.lbl_answer)).setText(rowModel.getAnswer());
            ((LinearLayout) itemView.findViewById(R.id.question_block)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (answer_block.getVisibility() == View.GONE) {
                        Screen_FAQ.this.viewAnswer(pos);
                        img_faq.setImageResource(R.drawable.ic_faq_minus);
                        AnimationUtils.expand(answer_block);
                        return;
                    }
                    img_faq.setImageResource(R.drawable.ic_faq_plus);
                    AnimationUtils.collapse(answer_block);
                }
            });
            this.faq_block.addView(itemView);
        }
    }

    public void viewAnswer(int pos) {
        for (int k = 0; k < this.answer_block_lst.size(); k++) {
            if (k != pos) {
                this.img_faq_lst.get(k).setImageResource(R.drawable.ic_faq_plus);
                AnimationUtils.collapse(this.answer_block_lst.get(k));
            }
        }
    }
}
