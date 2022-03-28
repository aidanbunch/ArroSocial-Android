package com.aidanbunch.arrosocial.view.onboardingUC;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.aidanbunch.arrosocial.R;
import com.aidanbunch.arrosocial.utils.SharedPrefs;
import com.aidanbunch.arrosocial.utils.UtilsMethods;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class UserCreationOnboardingAdapter extends RecyclerView.Adapter<UserCreationOnboardingAdapter.OnboardingViewHolder> {

    private List<UserCreationOnboardingItem> onboardingItems;
    private Context adapterContext;

    public UserCreationOnboardingAdapter(List<UserCreationOnboardingItem> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @Override
    public UserCreationOnboardingAdapter.OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user_creation_onboarding, parent, false);
        adapterContext = parent.getContext(); //to get the activity context use this line.
        OnboardingViewHolder dataObjectHolder = new OnboardingViewHolder(view); //in this way you can use the holder object.

        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserCreationOnboardingAdapter.OnboardingViewHolder holder, int position) {
        holder.setOnBoardingData(onboardingItems.get(position));
    }


    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {

        public TextView onboardingTitle;
        public TextInputEditText firstName;
        public TextInputEditText lastName;
        public AppCompatButton onboardingImagePreview;
        public TextView onboardingImagePickerTitle;
        public AppCompatButton shuffleBtn;

        OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            onboardingTitle = itemView.findViewById(R.id.onboardingTitle);
            firstName = itemView.findViewById(R.id.onboardingFirstName);
            lastName = itemView.findViewById(R.id.onboardingLastName);
            onboardingImagePreview = itemView.findViewById(R.id.onboardingImagePreview);
            onboardingImagePickerTitle = itemView.findViewById(R.id.onboardingImagePickerTitle);
            shuffleBtn = itemView.findViewById(R.id.shuffleBtn);

            if(UserCreationOnboardingItem.pageCount == 0) {
                firstName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        UserCreationOnboardingItem.userNameData = editable.toString();
                    }
                });
            }
            else if(UserCreationOnboardingItem.pageCount == 1) {
                firstName.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        UserCreationOnboardingItem.firstNameData = editable.toString();
                    }
                });
            }
            lastName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    UserCreationOnboardingItem.lastNameData = editable.toString();
                }
            });
            SharedPrefs.instance().storeValueString("generated_profile_picture_background_in_hex", "#7927e7");
            shuffleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String randColorHex = UtilsMethods.generateRandomColor();
                    SharedPrefs.instance().storeValueString("generated_profile_picture_background_in_hex", randColorHex);
                    ColorStateList csl = ColorStateList.valueOf(Color.parseColor(randColorHex));
                    onboardingImagePreview.setBackgroundTintList(csl);
                }
            });
        }

        void setOnBoardingData(UserCreationOnboardingItem onBoardingItem){
            onboardingTitle.setText(onBoardingItem.getOnboardingTitle());
            firstName.setHint(onBoardingItem.getOnboardingFirstName());
            lastName.setHint(onBoardingItem.getOnboardingLastName());
            onboardingImagePreview.setBackground(onBoardingItem.getOnboardingImagePreview());
            onboardingImagePickerTitle.setText(onBoardingItem.getOnboardingImageTitle());
            shuffleBtn.setText(onBoardingItem.getButtonText());
            shuffleBtn.setBackgroundResource(onBoardingItem.getButtonBackground());
            if (onBoardingItem.getButtonVisibility() == 0) {
                shuffleBtn.setVisibility(View.VISIBLE);
            }
            if(onBoardingItem.getImageButtonVisibility() == 0) {
                onboardingImagePreview.setVisibility(View.VISIBLE);
            }
            if(onBoardingItem.getFirstNameVisibility() == 1) {
                firstName.setVisibility(View.INVISIBLE);
            }
            if(onBoardingItem.getLastNameVisibility() == 1) {
                lastName.setVisibility(View.INVISIBLE);
            }
            if(!onBoardingItem.isButtonEnabled()) {
                shuffleBtn.setEnabled(false);
            }
            if(!onBoardingItem.isImageButtonEnabled()) {
                onboardingImagePreview.setEnabled(false);
            }
        }
    }
}

