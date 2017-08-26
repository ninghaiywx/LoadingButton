# LoadingButton

Gradle引入
<pre>compile 'me.ywx.LoadingButton:viewlibrary:1.0.3'</pre>

Xml引入
```xml
<com.example.ywx.viewlibrary.LoadingButton
        android:id="@+id/loadButton"
        app:button_background="#FFF000" //按钮主题背景色,必须是16进制颜色代码而不是int的color
        app:textSize="20sp"   //按钮上的字的大小
        app:load_color="#000000"    //加载进度条的颜色，必须是16进制颜色代码而不是int的color
        app:textColor="#FFFFFF"     //按钮上字体颜色，必须是16进制颜色代码而不是int的color
        app:text="点击加载"        //按钮上的文字    
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
```


基本使用
```java
public class MainActivity extends AppCompatActivity {
    private LoadingButton loadingButton;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.button);
        loadingButton=(LoadingButton)findViewById(R.id.loadButton);
        //设置监听，会在点击按钮后触发(不设置监听点击无效果)
        loadingButton.setOnButtonClickListener(new LoadingButton.OnButtonClickListener() {
            @Override
            public void onButtonClick() {

            }
        });
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingButton.reset();//重置按钮回到未加载的状态
            }
        });
        //字体的大小和字体的内容可以在代码中设置
    }
}
```
