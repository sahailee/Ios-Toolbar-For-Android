package com.android.iostoolbar;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class IosToolbar extends Toolbar {
    private String title;
    private String upButtonText;
    private boolean showSearchBar;

    private TextView smallTitle;
    private TextView largeTitle;
    private SearchView searchBar;
    private Button upButton;
    private View scrollingView;
    private Button searchCancelButton;
    private boolean showUpButton;
    private ScrollView scrollView;
    private ConstraintLayout toolbar;
    private int defaultBottomWithSearch;
    private int defaultBottomNoSearch;
    private int defaultBottom;
    private float maxSearchOffsetWithBack;
    private float maxSearchOffsetWithOutBack;
    private Typeface type;
    private float previousOffset = 0;
    private boolean isSwipeDown = true;

    public IosToolbar(Context context) {
        super(context);
        initializeViews(context);
    }

    public IosToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
        initializeViews(context, attrs);
    }

    public IosToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
        initializeViews(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Inflates the views in the layout.
     *
     * @param context
     *           the current context for the view.
     */
    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.ios_toolbar, this);
    }

    private void initializeViews(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IosToolbar, 0, 0);
        try {
            title = ta.getString(R.styleable.IosToolbar_title);
            title = title == null ? "Title":title;
            upButtonText = ta.getString(R.styleable.IosToolbar_up_button_text);
            upButtonText = upButtonText == null ? "Back":upButtonText;
            showUpButton = ta.getBoolean(R.styleable.IosToolbar_show_up_button, false);
            showSearchBar = ta.getBoolean(R.styleable.IosToolbar_show_search_bar, false);
        } finally {
            ta.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        smallTitle = findViewById(R.id.small_title);
        largeTitle = findViewById(R.id.large_title);
        searchBar = findViewById(R.id.search_bar);
        searchCancelButton = findViewById(R.id.cancel_button);
        scrollView = findViewById(R.id.title_scroll_view);
        upButton = findViewById(R.id.up_button);
        toolbar = findViewById(R.id.entire_toolbar);
        upButton.setCompoundDrawableTintList(ColorStateList.valueOf(Color.BLACK));
        defaultBottomWithSearch = -1;
        defaultBottomNoSearch = -1;

        setTitle(title);
        setUpButtonText(upButtonText);
        if(showSearchBar) {
            enableSearchBar();
            setupSearchBar();
        }
        else {
            disableSearchBar();
        }
        if(showUpButton) {
            enableUpButton();
        }
        else {
            disableUpButton();
        }
        try {
            type = Typeface.createFromAsset(getContext().getAssets(),"sf-ui-bold.otf");
            setUpTextAttributes();
        } catch (Throwable e) {
        }
    }

    private void setUpTextAttributes(){
        largeTitle.setTypeface(type);
        smallTitle.setTypeface(type);

        largeTitle.setTextSize(35);
        largeTitle.setTextColor(Color.BLACK);
        smallTitle.setTextSize(18);
        smallTitle.setTextColor(Color.BLACK);
    }

    // Using generics to allow for any adapter we make so that we can configure search bar
    // on other views.
    public <T extends Filterable> void configureSearchView(final T...adapters) {
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                for(T adapter: adapters) {
                    adapter.getFilter().filter(s);
                }
                return true;
            }
        });
    }

    private void setupSearchBar() {
        searchBar.setOnQueryTextFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    if(scrollingView != null) {
                        disableScrollingView();
                    }
                    smallTitle.setVisibility(GONE);
                    largeTitle.setVisibility(GONE);
                    upButton.setVisibility(GONE);
                    setMenuButtonVisibility(false);
                    searchCancelButton.setVisibility(VISIBLE);
                }
            }
        });
        searchCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAction();
                if(scrollingView != null) {
                    setupScrollingView();
                }
            }
        });

    }

    private void cancelAction () {
        smallTitle.setVisibility(INVISIBLE);
        largeTitle.setVisibility(VISIBLE);
        if(showUpButton) {
            upButton.setVisibility(VISIBLE);
        }
        else {
            upButton.setVisibility(INVISIBLE);
        }
        setMenuButtonVisibility(true);
        searchCancelButton.setVisibility(GONE);
        searchBar.setQuery("", false);
        searchBar.clearFocus();
    }



    public void setTitle(String title) {
        this.title = title;
        smallTitle.setText(title);
        largeTitle.setText(title);
    }

    public void setScrollingView(View scrollingView) {
        this.scrollingView = scrollingView;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setupScrollingView() {
        if(scrollingView instanceof RecyclerView) {
            scrollingView.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int x, int y, int x2, int y2) {
                    updateHeaderFromScroll(((RecyclerView)view).computeVerticalScrollOffset());

                }
            });
        }
        else {
            scrollingView.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int x, int y, int x2, int y2) {
                    updateHeaderFromScroll(view.getScrollY());
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void disableScrollingView() {
        largeTitle.setTranslationY(0);
        searchBar.setTranslationY(0);
        if(defaultBottomWithSearch > 0) {
            toolbar.setMaxHeight(defaultBottomWithSearch);
        }
        else {
            toolbar.setMaxHeight(Resources.getSystem().getDisplayMetrics().heightPixels / 4);
        }
        smallTitle.setVisibility(INVISIBLE);
        scrollingView.setOnScrollChangeListener(null);
        previousOffset = 0;
        isSwipeDown = true;
    }

    public void enableSearchBar() {
        searchBar.setVisibility(View.VISIBLE);
    }

    public void disableSearchBar() {
        cancelAction();
        searchBar.setVisibility(View.GONE);
        searchBar.setQuery("", false);
    }

    public void enableUpButton() {
        upButton.setVisibility(VISIBLE);
        showUpButton = true;
    }

    public void disableUpButton() {
        upButton.setVisibility(INVISIBLE);
        showUpButton = false;
    }

    public void setUpButtonText(String text) {
        this.upButtonText = text;
        upButton.setText(text);
    }

    public void setUpButtonOnClickListener(OnClickListener onClickListener) {
        upButton.setOnClickListener(onClickListener);
    }

    private void setMenuButtonVisibility(boolean visible) {
        for(int i = 0; i < getMenu().size(); i++) {
            getMenu().getItem(i).setVisible(visible);
        }
    }

    private void updateHeaderFromScroll(float offset) {
        if(defaultBottomWithSearch < 0 && searchBar.getVisibility() == VISIBLE) {
            defaultBottomWithSearch = toolbar.getBottom();
            maxSearchOffsetWithBack = searchBar.getY() - upButton.getBottom();
            maxSearchOffsetWithOutBack = searchBar.getY() - smallTitle.getY();
        }
        else if(defaultBottomNoSearch < 0 && searchBar.getVisibility() == GONE) {
            defaultBottomNoSearch = toolbar.getBottom();
        }
        defaultBottom = searchBar.getVisibility() == VISIBLE ? defaultBottomWithSearch : defaultBottomNoSearch;
        float maxSearchOffset = showUpButton ? maxSearchOffsetWithBack : maxSearchOffsetWithOutBack;
        if(offset - previousOffset > - 40 && offset - previousOffset < 1 && isSwipeDown) {
            return;
        }
        else if(previousOffset - offset > - 40 && previousOffset - offset < 1 && !isSwipeDown) {
            return;
        }
        else if(previousOffset == maxSearchOffset && offset > previousOffset) {
            return;
        }
        if(offset - previousOffset >= 0) {
            isSwipeDown = true;
        }
        else {
            isSwipeDown = false;
        }
        if(searchBar.getVisibility() == GONE) {
            largeTitle.setTranslationY(-2.0f * offset);
            offset = Math.min(offset, maxSearchOffset);
            toolbar.setMaxHeight((int) (defaultBottom + (-1 * offset)));
        }
        else {
            largeTitle.setTranslationY(-1.0f * offset);
            offset = Math.min(offset, maxSearchOffset);
//            searchBar.setTranslationY(-55);
            toolbar.setMaxHeight((int) (defaultBottom + (-1 * offset)));
//            searchBar.setTranslationY(-55);
        }
        previousOffset = offset;
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        if (largeTitle.getLocalVisibleRect(scrollBounds)) {
            // Any portion of the imageView, even a single pixel, is within the visible window
            smallTitle.setVisibility(INVISIBLE);
        } else {
            smallTitle.setVisibility(VISIBLE);
        }
    }

}
