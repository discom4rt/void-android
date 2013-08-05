package io.morgan.Void;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by mobrown on 6/22/13.
 */
public class PostAdapter extends ArrayAdapter<Post> {

    public Context context;
    public int layoutResourceId;
    public ArrayList<Post> data;
    public PostListView listView;

    public PostAdapter(Context context, int layoutResourceId, ArrayList<Post> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PostHolder holder;

        if(convertView == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            holder = new PostHolder();
            holder.postImage = (ImageView)convertView.findViewById(R.id.post_image);

            WindowManager wm = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();

            holder.postImage.getLayoutParams().height = display.getWidth();
            holder.postLocation = (TextView)convertView.findViewById(R.id.post_location);

            convertView.setTag(holder);
        }
        else
        {
            holder = (PostHolder) convertView.getTag();
        }

        Post post = data.get(position);
        holder.postLocation.setText(post.location);

        if(post.imageMap == null) {
            holder.postImage.post(new Runnable() {
                @Override
                public void run() {
                    holder.postImage.setImageDrawable(context.getResources().getDrawable(R.drawable.loading));
                    AnimationDrawable rocketAnimation = (AnimationDrawable) holder.postImage.getDrawable();
                    holder.postImage.setVisibility(View.INVISIBLE);
                    holder.postImage.setVisibility(View.VISIBLE);
                    rocketAnimation.start();
                    listView.scrollBy(0, 0);
                }
            });


            post.fetchImageMap(holder.postImage.getLayoutParams().height, holder.postImage.getLayoutParams().height, new Post.Callback() {

                @Override
                public void onSuccess(Post post) {
                    holder.postImage.setImageBitmap(post.imageMap);
                    PostAdapter.this.notifyDataSetChanged();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(context, "Sorry, failed to get one of your images", Toast.LENGTH_LONG).show();
                    // set post image to question mark or something
                    // set location text to something like, tap to reload
                    // implement the tap to reload
                }
            });
        } else {
            holder.postImage.setImageBitmap(post.imageMap);
        }

        return convertView;
    }

    /**
     * Remove the post at the given index
     *
     * @param index The index of the post to be removed
     */
    public void removeAt(int index) {
        data.remove(index);
    }

    static class PostHolder {
        ImageView postImage;
        TextView postLocation;
    }
}
