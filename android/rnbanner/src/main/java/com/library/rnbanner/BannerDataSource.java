package com.library.rnbanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * banner bean
 * viewType
 * 1：正常网络图片
 * 2：RN view
 * 3：video
 */
public class BannerDataSource {

    public int rnIndex;//第几个位置需要使用RN View
    public String imageUrl;
    public String title;
    public int viewType;

    public BannerDataSource() {
    }

    public BannerDataSource(int rnIndex, String title, int viewType) {
        this.rnIndex = rnIndex;
        this.title = title;
        this.viewType = viewType;
    }

    public BannerDataSource(String imageUrl, String title, int viewType) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.viewType = viewType;
    }


    /**
     * 仿淘宝商品详情第一个是视频
     */
    public static List<BannerDataSource> getTestDataWithVideo() {
        List<BannerDataSource> list = new ArrayList<>();
        list.add(new BannerDataSource("http://vfx.mtime.cn/Video/2019/03/09/mp4/190309153658147087.mp4", "第一个放视频", 3));
        list.add(new BannerDataSource("https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg", null, 1));
        list.add(new BannerDataSource("https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg", null, 1));
        list.add(new BannerDataSource("https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg", null, 1));
        list.add(new BannerDataSource("https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg", null, 1));
        return list;
    }

    public static List<BannerDataSource> getTestUrls() {
        List<BannerDataSource> list = new ArrayList<>();
        list.add(new BannerDataSource("https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg", null, 1));
        list.add(new BannerDataSource("https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg", null, 1));
        list.add(new BannerDataSource("https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg", null, 1));
        list.add(new BannerDataSource("https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg", null, 1));
        list.add(new BannerDataSource("https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg", null, 1));
        return list;
    }

    public static List<BannerDataSource> getVideos() {
        List<BannerDataSource> list = new ArrayList<>();
        list.add(new BannerDataSource("http://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4", null, 3));
        list.add(new BannerDataSource("http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4", null, 3));
        list.add(new BannerDataSource("http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4", null, 3));
        list.add(new BannerDataSource("http://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4", null, 3));
        list.add(new BannerDataSource("http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4", null, 3));
        list.add(new BannerDataSource("http://vfx.mtime.cn/Video/2019/03/14/mp4/190314102306987969.mp4", null, 3));
        return list;
    }


    public static List<String> getColors(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(getRandColor());
        }
        return list;
    }

    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *
     * @return String
     */
    public static String getRandColor() {
        String R, G, B;
        Random random = new Random();
        R = Integer.toHexString(random.nextInt(256)).toUpperCase();
        G = Integer.toHexString(random.nextInt(256)).toUpperCase();
        B = Integer.toHexString(random.nextInt(256)).toUpperCase();

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        return "#" + R + G + B;
    }
}
