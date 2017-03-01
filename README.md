# FilterDemo
一个仿伙伴云表格的筛选框架

最近在做一个ERP的项目，产品要求做一个类似京东，淘宝，伙伴云表格的筛选控件，如下图所示：

![S70301-160019.jpg](http://upload-images.jianshu.io/upload_images/3287039-3e460456985cbeac.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##思路
   这个页面其实一个主页面的Activity，右滑弹出一个抽屉DrawerLayout，抽屉其实是一个fragment，我们主要的布局就在fragment里面。而这个筛选控件其中的一个其实是下拉菜单。当然原生的spinner太丑而且不符合要求，所以我用一个LinearLayout包含一个TextView（drawableRight下拉图标），加一个listview来构造。


##上效果图，详情请见代码
![123.jpg](http://upload-images.jianshu.io/upload_images/3287039-13e943e00acdc5f4.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
