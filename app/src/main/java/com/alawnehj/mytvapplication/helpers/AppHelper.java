package com.alawnehj.mytvapplication.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alawnehj.mytvapplication.models.TvShow;
import com.alawnehj.mytvapplication.models.TvShowDetails;

public class AppHelper {
    public static <T> void intent(Context context, Class<T>endClass){
        context.startActivity(new Intent(context,endClass));
    }

    public static <T> void intentWithExtras(Context context , Class<T> endClass, TvShow tvShow){
        Intent intent = new Intent(context,endClass);
        intent.putExtra("tvShow",tvShow);
        context.startActivity(intent);

    }


}
