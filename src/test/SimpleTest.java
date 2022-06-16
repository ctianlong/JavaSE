package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.netease.snailreader.common.component.uri.UriParams;
import com.netease.snailreader.common.component.uri.UriUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.nodes.Document;
import org.junit.Test;
import util.common.NumberUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/5/28
 */
public class SimpleTest {

    @Test
    public void test1111() {
        String s = "{\"_hostname\":\"yuedu9.v1.literature.jda.vpc\",\"_body\":\"2021-12-02 11:20:37,934  INFO (LogUtil.java:26)[qaStatistic] - [j-2], userId:1070, channelId:7, linkId:0, timestamp:1638415237934, common:{\\\"androidId\\\":\\\"36cecec57756f3d27\\\",\\\"osType\\\":\\\"android\\\",\\\"model\\\":\\\"X23\\\",\\\"versionCode\\\":24,\\\"packageName\\\":\\\"com.kuailai1\\\",\\\"manufacturer\\\":\\\"vivo\\\",\\\"brand\\\":\\\"vivo\\\",\\\"osVersion\\\":\\\"10\\\",\\\"clientTime\\\":1638415234389}, message:{\\\"bannerId\\\":\\\"665\\\",\\\"order\\\":2,\\\"configType\\\":0,\\\"sourceUuid\\\":\\\"qa_ZWwEKZxHinD8_zwkKD0kyRDacaH-bHcnOi-eRNsm-685dy8EKQ\\\"}\"}";
        String s1 = "^\\{\"\\w+\":\".*\"\\,\"\\w+\":\".*\\[\\w+\\]\\s+\\-\\s+\\[(\\w+\\-\\d+)\\],.*}$";
        System.out.println(Pattern.compile(s1).matcher(s).matches());
    }

    @Test
    public void testParallCollect() {
//        List<Integer> list = Lists.newArrayList();
//        for (int i = 0; i < 1000; i++) {
//            list.add(i);
//        }
//        Set<Integer> collect = Lists.partition(list, 1000).parallelStream().flatMap(Collection::stream).collect(Collectors.toSet());
//        System.out.println(collect.size());

//        List<Integer> result = list.parallelStream().filter(i -> i % 2 == 1).skip(100).limit(20).collect(Collectors.toList());
//        System.out.println(result.size());
//        result.forEach(System.out::println);

        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
        String currentDay = formatter.format(new Date());

        List<String> list1 = Lists.newArrayList("a", "b", "c", "d", "e");
        Iterator<String> iterator = list1.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            System.out.println(s);
            if ("c".equals(s)) {
                iterator.remove();
            }
        }
        System.out.println(list1);


//        System.out.println(result);
    }

    @Test
    public void test111() {
        long sell = 2297748905L;
        long afterTaxSell = new BigDecimal(sell * 10000).
                divide(new BigDecimal(10600), 0, RoundingMode.HALF_EVEN).longValue();
        System.out.println(afterTaxSell);
        System.out.println(null instanceof String);
    }

    @Test
    public void testUrl() throws MalformedURLException {
        URL url = new URL("http://mmbiz.qpic.cn/mmbiz_jpg/6icopJdlxe4yyf1YYAhf0BA3tA1cLg90fgoXaKHT0BFRewQBrpQUkq6PWEhX0x7QaQhSKeWYoPhfrojiaLV6ovzQ/0?wx_fmt=jpeg");
        System.out.println(url.getHost());
        System.out.println(url.getPath());
        System.out.println(url.getPort());
        System.out.println(url.getProtocol());
    }

    @Test
    public void generate() {
        String t =
                "@JsonIgnore\n" +
                        "public String getFormatD{n}() {\n" +
                        "    return doGetFormatDn({n});\n" +
                        "}\n";
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 365; i++) {
            sb.append(t.replace("{n}", String.valueOf(i)));
        }
        System.out.println(sb.toString());

    }

    @Test
    public void testJsoup() {
        String s = "<p>　　这更让我感觉这个庙里供奉的主神实在是太过嚣张了一点，如果他的法相真身就是在这个圆柱之上的话，那就真的有一种高高在上的感觉，不仅让那些神戴上枷锁跪拜，更是居高临下的看着他们。</p><p>　　“这到底是谁啊这么嚣张？”我念叨道，我心想就是玉皇大帝，其他的玉帝庙里其他的神像也只是稍微比玉帝低一点，也不见的就要坐这么高，我甚至感觉他是诚心要这样子，为的就是蔑视众神？</p><p>　　“不知道，但是邪乎的很，我要不要上去看看？”李青问我道。</p><p>　　我虽然想，但是我还是对李青摇了摇头道：“你都已经感觉到危险了，我相信你的第六感，别上去了，好奇心杀死猫。”</p><p>　　李青耸了耸肩道：“说实话，还没跟神仙交过手，真死在他们手里也值了，都到这地步了，我要是不上去看看估计要很久都睡不着，我要是出事的话，你就想办法出去。”</p><p>　　听到他这话，我更不乐意他上去了，主要是这个地下的古庙四处都透着诡异，但是我还没来得及阻拦李青，他就走到了柱子边上拉了一下皮带，之后他以一个非常奇怪的姿势往上攀爬。</p><p>　　他的手脚抓在石柱上，问题是石柱上没有任何的着力点，从我这边看，他更像是一个壁虎一样的往上在爬，以前看电视上听他们说过一种可以飞檐走壁的功夫叫壁虎爬墙功，想必就是如此，看来李青这个人最拿手的真不是他的那脚回旋踢，而是他的身法，不论是沾衣十八跌还是现在的壁虎爬墙，都极具欣赏价值，也就是秀。</p><p>　　他的动作逐渐加快，我就这么看着他最后手抓住了那石柱边缘的莲花边，整个人就攀了上去，这时候我的心都已经提到了嗓子眼儿，我这才想起来我的背包里准备的有绳子，都想抽自己两巴掌，我应该让他把绳子带上去，然后把我拉上去的嘛！</p><p>　　我就这么眼巴巴的抬头看着上面，但是跟刚才一样，站在我现在的位置根本就看不到上面的东西，我退后了几步却发现依旧是如此，视线被柱子边缘的莲花花瓣完全给挡住了。我此时就算心急也没有办法，只能等，这个柱子并不算特别巨大，只不过是在这个庙中才显的它大，李青用上几分钟就可以在柱子顶上绕一个来回，但是转眼间十分钟过去了，李青还是没有动静。</p><p>　　我甚至都没有听到什么打斗声，心道上面真的是个跟玉皇大帝一样的神仙？李青完全不是对手直接就被两根手指给捏死了？我就对着上面大叫道：“李青，你还好吗？”</p><p>　　但是没有回应，我又叫了几声，依旧是没有。</p><p>　　这下我真的着急了，我想上去看看，就试着用李青刚才的姿势往上爬，但是我发现我连一点都不上去。</p><p>　　这时候，已经过去了半个小时，我心中那不详的预感越演越烈，不仅是因为李青死了我唯一的“保镖”没了，而是我真的担心好好的一个人就这么悄无声息的交代在了这里，我又叫了几声，依旧是没有任何的回应。</p><p>　　我渐渐的感觉到了绝望。</p><p>　　我不再呼叫，因为这时候呼叫已经没用了，但是我停下来之后，却感觉四周静悄悄的，总感觉背后有一双眼睛在盯着我，我回头一看，后面两侧依旧是那些跪着戴着枷锁的神像，刚才看这些神像我是感觉可怜，现在却感觉十分的阴森恐怖。</p><p>　　这时候，唯一能陪着我的，就是钻在我怀里的这个小家伙，他似乎感觉到了我的紧张，探出了脑袋看着我，我摸了摸他的头道：“小家伙，估计我也要死在这里了。”</p><p>　　我咬了咬牙，一手拿着匕首一手抓着手电，我不能坐以待毙，而且我也救不了李青，现在我能做的就是尽力出去，再找人回来，我打着手电，忍着心中那剧烈的恐惧在四周转了一圈儿，却发现什么都没有，除了我们进来的那道石门之外根本就没有出去的路，但是出那道石门的话唯一的出口又被陈石头那家伙给堵上了。</p><p>　　难道天要绝我？</p><p>　　最后，我一屁股坐在了圆柱边上，大哥到现在都还没有来，我甚至担心他自己都遭遇了不测，而在这里面我的主心骨李青现在又生死未必，我摸着这个小家伙的脑袋问它道：“你知道出口在那里吗？再不出去，我就算不被闷死饿死，也会被吓死。”</p><p>　　这个小家伙伸出了前爪往上指了指，他指的方向，是圆柱的上面。</p><p>　　“你意思是出口在上面吗？我也想过，不过我上不去啊，就算我能上去，李青都被那么随意弄死了更别说我。”我道。</p><p>　　谁知道这小家伙还在往上指，并且焦急的拉着我的衣领，我这才反应过来它其实是让我往上面看。</p><p>　　我打着手电往上一看，手电光照到的地方，我看到在那莲花花瓣的中间，伸出来一张人脸，这时候的一张人脸把我吓了一跳，但是我马上就万般的激动，因为我看到这张人脸是我大哥！</p><p>　　我张嘴就要叫，大哥却在上面对我做了一个噤声的手势，接着他轻声的对我道：“把绳子丢上来，我拉你上来，记住，别回头。”</p><p>　　他是制止我回头的，但是好死不活的是在听到他说别回头三个字的时候，我竟然傻逼的下意识的回头看了一眼，这一回头，就看到两个人影就站在我背后的通道上，这两个人，正是那石门门口的两个人形的侍卫！</p><p>　　更可怖的是，那些跪下的神像，竟然一个个的开始眼睛往外冒血。</p><p>　　这一幅景象，跟风水眼上的龙头碑龙眼冒血一模一样！这景象几乎把我吓呆了。</p><p>　　“快把绳子丢上来，快！”大哥在上面道。</p><p>　　我手忙脚乱的从背包里拿出绳子，随手就往上一丢，但是绳子很轻，根本就丢不高，我想找块石头绑上，但是四周真的连一个像样的石头都没有，只他娘的有一个香炉，问题是香炉的话我抱都抱不起来更别说丢了。</p><p>　　可是这两个侍卫已经朝我走了过来，他们俩的眼珠子都变成了红色，我不知道怎么回事，咋忽然石头就好像是僵尸一样会走了呢？但是不管原因如何，就我这小身板，这石像要真跟人一样给我一拳都能把我给活活的打死！</p><p>　　可是我真的没办法把石头丢上去啊！眼见着这两个石头人都</p><p>　　我急的眼泪都要出来了，就在这时候，我怀里的小家伙忽然窜了出来，它看了看我，然后一口咬住了绳子的一端使劲儿拉，示意我把绳子给解开！好在我这时候虽然害怕脑袋却十分的清醒，我意识到这小家伙是要帮我把绳子的那一端给大哥送上去，我赶紧解开了绳子，果不其然，这个小家伙叼住了绳子的一端，咬着牙似乎下定了决心一样的往柱子上爬去。</p><p>　　我不知道黄鼠狼会不会爬树，但是这小家伙爬这个石柱却很吃力，它爬了几步，就滑落下来，继续冲，又继续的往下滑，而这时候，我已经闻到石头人身上的气息，这是一种很让人难受的味道。</p><p>　　它的爬不上去，几乎让我放弃了地方，就在这时候，小家伙眼神坚毅的退了几步，开始狂奔，这一次，它似乎是掌握了什么要领，终于是在这石柱上可以跑的飞快，最终它把绳子的那一端交给了大哥，但是就在大哥伸手要把它给拉上去的时候，它却摇了摇头，沿着绳子顺了下来，之后跳到了地上。</p><p>　　它就这么站在地上，眼巴巴的看着我。</p>";
        System.out.println(countInPlainTextMode(s));
    }

    static public int countInPlainTextMode(String content) {
        Document document = Jsoup.parseBodyFragment(content);
        document.select("a").tagName("span");
        HtmlToPlainText formatter = new HtmlToPlainText();
        return formatter.getPlainText(document).replaceAll("\\s", "").length();
    }

    @Test
    public void testEmoji() throws UnsupportedEncodingException {
        String s = "\uD83D\uDE00测试一下测试一下\uD83D\uDE00测试一下测试一下测";
        System.out.println(s.getBytes().length);
        System.out.println(s.getBytes("utf-8").length);
        Map<String, Object> dd = Maps.newLinkedHashMap();
        dd.put("da", "dd");
        dd.put("as", "dd");
        dd.put("fdaf", "dd");
        dd.put("daf", "dd");
        System.out.println(JSONObject.toJSONString(dd));

        String json = "{\"aaa\":\"dd\"}";
        JSONObject.parseObject(json, User.class);
    }



    @Test
    public void testDateFormat() throws ParseException {
//        LocalDateTime time = LocalDateTime.parse("15/Jun/2020:17:05:40 +0800", DateTimeFormatter.ofPattern("dd/MMM/YYYY:HH:mm:ss Z"));
//        System.out.println(time);
//        String dtime1 = "26/Dec/2010:15:49:18 +0800";
//        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
//        Date date = sdf1.parse(dtime1);
//        System.out.println(date);
//        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String sDt = sdf2.format(date);

//        Set<User> set = new HashSet<>();
//        User user = new User(1L, "1");
//        User user2 = new User(1L, "2");
//        set.add(user);
//        if (set.contains(user2)) {
//            set.remove(user2);
//            set.add(user2);
//            System.out.println("true");
//        } else {
//            set.add(user2);
//            System.out.println("false");
//        }
//        System.out.println(set);

        Set<User> set = new TreeSet<>(Comparator.comparing(User::getName));
        User user = new User(1L, "1");
        User user2 = new User(1L, "2");
        set.add(user);
        set.add(user2);



        System.out.println(set);
    }

    @Test
    public void testaaa() throws Exception {
        int i = 0;
        Set<String> set = new LinkedHashSet<>();
        Scanner scanner = new Scanner(new FileInputStream("C:\\Users\\ctl\\Desktop\\aa1.txt"));
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
//            System.out.println(s);
            set.add(s);
            i++;
        }
        System.out.println(i);
        System.out.println(set.size());
//        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:\\Users\\ctl\\Desktop\\1-sid-filter.txt")));
//        for (String s : set) {
//            wr.write(s);
//            wr.write("\n");
//        }
//        wr.close();
    }


    @Test
    public void testArray() {
//        long[] aa = new long[0];
//        System.out.println(aa.length);
//        User user = new User();
//        int len = user.getAa().length;
//        Map<String, String> map = new HashMap<>();
//        map.put("1", "a");
//        map.put("22", "b");
//        map.put("33", "c");
//        map.put("444", "d");
//        map.keySet().removeIf(k -> k.length() == 1);
//        System.out.println(map);
//        String k = null;
//        System.out.println(map.get(k));

//        List<Long> list = Lists.newArrayList(1L, null, 2L);
//        System.out.println(list.size());
//        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
//        System.out.println(list);

//        Integer i = null;
//        System.out.println(i == 10000);

    }

    @Test
    public void test() {
        Pattern compile = Pattern.compile("((([0-9A-Za-z]{2,10})_)?([A-Z]{2}|EUEX)_)?[0-9a-fA-F]{32}");
        System.out.println(compile.matcher("CN_a79cc95ace8d5b2726f2ad8171a1ffff").matches());
    }

    public static class User {
        private Long id;
        private String name;
        private long[] aa;

        public long[] getAa() {
            return aa;
        }

        public void setAa(long[] aa) {
            this.aa = aa;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public User(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public User() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            User user = (User) o;
            return Objects.equals(id, user.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Test
    public void testLinkHashSet() {
        Set<String> set = Sets.newLinkedHashSet();
        set.add("b11");
        set.add("b11");
        set.add("b11");
        set.add("a22");
        set.add("a11");
        set.add("bbb");
        set.add("bbb");
        set.add("bbb");
        set.add("bbb");
        set.add("bbb");
        set.add("dddd");
        set.add("sjfslkfjsk");
        List<String> list = Lists.newArrayList();
        int i = 1;
        for (String s : set) {
            list.add(s + "-" + i++);
        }
        System.out.println(list);
    }

    @Test
    public void testFindAny() {
        List<String> list = Lists.newArrayList("1", "2", "3", "4", "5", "6", "7");
        for (int i = 0; i < 10; i++) {
//            System.out.println(list.stream().findAny().get());
//            Collections.shuffle(list);
//            System.out.println(list.get(0));
            System.out.println(list.get(new Random().nextInt(list.size())));
        }
    }

    @Test
    public void test11() throws IOException {
        new File("C:\\Users\\ctl\\Desktop\\test.txt").createNewFile();
    }

    private static int num = 0;

    @Test
    public void checkUserId() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\ctl\\Desktop\\云阅读暗扣\\junkuserid.txt")));
        String line;
        int count = 0;
        List<Long> userIds = new ArrayList<>(1000);
        while ((line = reader.readLine()) != null) {
            userIds.add(Long.valueOf(line));
            count++;
            if (userIds.size() == 1000) {
                importUser(userIds);
                userIds.clear();
            }
        }
        if (userIds.size() > 0) {
            importUser(userIds);
        }
        reader.close();
        System.out.println(count);
        System.out.println(num);
    }

    private void importUser(List<Long> userIds) {
        num++;
        System.out.println(userIds.size());
    }

    @Test
    public void test1() {
        System.out.println(new BigDecimal("222.22").multiply(new BigDecimal(100)).setScale(0, RoundingMode.DOWN).intValueExact());
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList("a", "b", "c", "d", "e", "f"));
        list = list.stream().limit(3).collect(Collectors.toList());
        System.out.println(list);

        if (list == null) {
        }

        if (true) {
            System.out.println();
        }

    }

    @Test
    public void test2() {
        System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Map<String, String> map = new HashMap<>();
        map.put("a", "b");
        map.put("c", "d");
        System.out.println(Joiner.on("\n").withKeyValueSeparator("=").join(map));
    }

    @Test
    public void test3() throws NoSuchFieldException, IllegalAccessException {
        Map<String, String> m = new HashMap<>(1);
        m.put("k", "v");
        Class<? extends Map> a = m.getClass();
        System.out.println(a);
        Field threshold = a.getDeclaredField("threshold");
        threshold.setAccessible(true);
        Object o = threshold.get(m);
        System.out.println(o);
    }

    @Test
    public void test4() {
        Map<String, Map<String, String>> m = new HashMap<>();
        m.put("a", new HashMap<String, String>() {
            {
                put("test1", "value1");
            }
        });
//        m.put("b", new HashMap<String, String>() {
//            {
//                put("btest1", "bvalue1");
//            }
//        });
        m.computeIfAbsent("b", k -> new HashMap<>()).put("test2", "value2");


        System.out.println(m);
    }


    @Test
    public void testNumberFormat() {
        int p = 4;
        StringBuilder pattern = new StringBuilder("0");
        if (p > 0) {
            pattern.append(".");
            for (; p > 0; pattern.append("0"), p--);
        }
//        pattern.append("%");
        NumberFormat nf = new DecimalFormat(pattern.toString());
        nf.setRoundingMode(RoundingMode.HALF_UP);
//        System.out.println(nf.format(0.98545 * 100));

        System.out.println(NumberUtils.formatPercent(12, 123, 0));

    }

    @Test
    public void testRandomString() {
        System.out.println(RandomStringUtils.random(8, true, false));
        System.out.println(RandomStringUtils.random(8, true, true));
        System.out.println(UUID.randomUUID().toString().replace("-", ""));
    }

    @Test
    public void testMutableInt() {
        Integer i = 3100;
        MutableInt m = new MutableInt();
        m.add(i);
        System.out.println(m.toInteger());
    }

    @Test
    public void testReadLine() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\ctl\\Desktop\\test.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("[" + line + "]");
        }
    }

    @Test
    public void testParallelStream() {
        List<Integer> a = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            a.add(i);
        }
        System.out.println(a);
        List<Integer> b = a.stream().filter(i -> i % 2 == 1).collect(Collectors.toList());
        System.out.println(b);
        // parallelStream 也会保持原有顺序
        List<Integer> c = a.parallelStream().filter(i -> i % 2 == 1).collect(Collectors.toList());
        System.out.println(c);
        // 测试方法引用
        TestFunctionInterface t = String::codePointCount;
    }

    @Test
    public void testParallelStreamToSort() {
        List<String> list = new ArrayList<>();
        list.add("C");
        list.add("H");
        list.add("A");
        list.add("A");
        list.add("B");
        list.add("F");
        list.add("");

//        list.parallelStream() // in parallel, not just concurrently!
//                .filter(s -> !s.isEmpty()) // remove empty strings
//                .distinct() // remove duplicates
//                .sorted() // sort them
//                .forEach(System.out::println); // print each item
//                .forEachOrdered(System.out::println);

        list = list.parallelStream() // in parallel, not just concurrently!
                .filter(s -> !s.isEmpty()) // remove empty strings
                .distinct() // remove duplicates
                .sorted() // sort them
                .collect(Collectors.toList());
        System.out.println(list);
    }

    @Test
    public void testSplitter() {
        String s = "a\n  \nb\n\nc\n";
        System.out.println(Splitter.on("\n").trimResults().omitEmptyStrings().split(s));
        System.out.println(Splitter.on("\n").omitEmptyStrings().trimResults().split(s));
    }

    @Test
    public void testUnion() {
//        HashSet<String> s1 = Sets.newHashSet("a", "b", "c");
//        HashSet<String> s2 = Sets.newHashSet("c", "d");
//        Sets.SetView<String> union = Sets.union(s1, s2);
//        System.out.println(union);
//        System.out.println(Sets.difference(s1, s2));
//        System.out.println(Sets.difference(s2, s1));
//        Integer i = null;
//        int ii = 0;
//        System.out.println(Objects.equals(i, ii));

        String s = "\u2B50签到送币aaaa";
        System.out.println(NOT_LETTER_NUMBER_CHIN.matcher(s).replaceAll(""));
        System.out.println(s.length());
        System.out.println(s.charAt(0));
        System.out.println(s.substring(0, 1));
        System.out.println(s.replace("\u2B50", ""));
        System.out.println(s.replaceAll("[^\u4E00-\u9FBF]", "x"));
        System.out.println("\u0030\u0039\u0041\u005A\u0061\u007A\u25A0");
    }

    @Test
    public void testSystem() {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }

    public static final Pattern NOT_LETTER_NUMBER_CHIN = Pattern.compile("[^\u4E00-\u9FBF\u0030-\u0039\u0041-\u005A\u0061-\u007A]");


    private static final Pattern p0 = Pattern.compile("[\\n|\\r|\\t|\\s]");
//    private static final Pattern p1 = Pattern.compile("</p><p>");
//    private static final Pattern p2 = Pattern.compile("<[/]?p>");
    private static final Pattern p1 = Pattern.compile("</[^>]*><[a-zA-Z!\\?][^>]*>");
    private static final Pattern p2 = Pattern.compile("<[a-zA-Z!\\/\\?][^>]*>");
    private static final Pattern p3 = Pattern.compile("&nbsp;");

    @Test
    public void testString() {
        StringBuilder sb = new StringBuilder();
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get("C:\\Users\\ctl\\Desktop\\bj1")), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        content = p0.matcher(content).replaceAll("");
        content = p1.matcher(content).replaceAll("\r\n");
        content = p2.matcher(content).replaceAll("");
        content = p3.matcher(content).replaceAll(" ");
        sb.append(content).append("\r\n\r\n");

        content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get("C:\\Users\\ctl\\Desktop\\bj2")), Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        content = p0.matcher(content).replaceAll("");
        content = p1.matcher(content).replaceAll("\r\n");
        content = p2.matcher(content).replaceAll("");
        content = p3.matcher(content).replaceAll(" ");
        sb.append(content).append("\r\n\r\n");

        try {
            Files.write(Paths.get("C:\\Users\\ctl\\Desktop\\result.txt"), sb.toString().getBytes(Charset.forName("UTF-8")), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegex() {
        String result = "ab|cd";
        System.out.println(result);
        result = result.replaceAll("\\|", "\\\\|");
        System.out.println(result);
    }

    @Test
    public void testListSub() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        System.out.println(list.stream().skip(5).collect(Collectors.toList()));
        System.out.println(list.subList(0,2));
        System.out.println(list);
    }

    @Test
    public void testGroupBy() {
        List<Person> ps = new ArrayList<>();
        ps.add(new Person(1L, "tt", 12));
        ps.add(new Person(2L, "ee", 13));
        ps.add(new Person(3L, "aa", 13));
        System.out.println(ps.stream().collect(Collectors.groupingBy(Person::getAge)));
    }

    @Test
    public void testSet() {
        Set<String> s1 = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        Set<String> s2 = new HashSet<>(Arrays.asList("a", "b"));
        s1.removeAll(s2);
        System.out.println(s1);
        System.out.println(s2);
    }

    class Person {
        private Long id;
        private String name;
        private Integer age;

        public Person(Long id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

    @Test
    public void testUrl111() throws UnsupportedEncodingException {
//        String s = "http://ddd.com/aa/bb";
//        String s1 = URLEncoder.encode(s, "UTF-8");
//        System.out.println(s1);
//        String s2 = URLEncoder.encode("redirect_url=" + s1, "UTF-8");
//        System.out.println(s2);

//        String ss = "神戒在手，   美女我有。\n" +
//                "高中生宋砚自从得到一枚戒指后，生活就发生了翻天覆地的变化，高冷校花，绝美老师，妩媚老板娘，暴力警花一一闯入他的生活。\n" +
//                "为此，宋砚非常苦恼，面对诸多美女，是收了，还是收了，还是收了？";
////        System.out.println(ss.length());
//        System.out.println(ss.replaceAll("[\\s\\t\\n\\r]", ""));
//        System.out.println(ss.replaceAll("[\\s\\t\\n\\r]", "").substring(0, 12));

//        System.out.println(System.currentTimeMillis());
//        System.out.println(6 * 30 * 24 * 3600 * 1000);
//        System.out.println(System.currentTimeMillis() - 6 * 30 * 24 * 3600 * 1000);
//        System.out.println(1595213768577L > System.currentTimeMillis() - 6 * 30 * 24 * 3600 * 1000);
        long[] longs = new long[3];
        System.out.println(longs[0]);
    }

    @Test
    public void testGG() {
        Map<String, String> map = Maps.newHashMap();
        map.put("key", null);
        System.out.println(map.containsKey("key"));
        System.out.println(map.containsKey("foo"));
        System.out.println(map.get("key"));
        System.out.println(map.get("foo"));
//        String[] s = StringUtils.split("第111章  冯宝宝和威廉姆斯", " ");
//        System.out.println(s.length);
    }

    @Test
    public void testJwt() throws InterruptedException {
        DecodedJWT decodedJwt = JWT.require(Algorithm.HMAC256("of=sAIN0U0^keZ@0LPGDe4V%QPD74Oc+0wWQuTgvD&9E*q0Ux-F@cRSLBS2-#yq!::pre"))
                    .build().verify("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMDc3NyIsImlhdCI6MTY1MTczMjkyNH0.Mo2K1gf6u5LuZTIaAoq71LacBLt47SLoyViw_RGXvEg");

        System.out.println(decodedJwt.getSubject());
//        for (int i = 1; i <= 100; i++) {
//            String secret = RandomStringUtils.randomAlphanumeric(64);
//            long start = System.currentTimeMillis();
//            String jwt = JWT.create()
//                    .withSubject(String.valueOf(i))
//                    .withExpiresAt(new Date(System.currentTimeMillis() + i * 100L))
//                    .withClaim("k", "" + i)
//                    .sign(Algorithm.HMAC256(secret));
//            System.out.println(System.currentTimeMillis() - start);
//        }
//        System.out.println(jwt);
//        Thread.sleep(6000);
//        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256("abc")).build().verify(jwt);
//        System.out.println(decodedJWT.getSubject());
//        System.out.println(decodedJWT.getClaim("k").asString());
//        System.out.println("------");
    }

    @Test
    public void testVariable() {
        testSS(1);
        testSS(1, 2);
        testSS();
    }

    private void testSS(int... ss) {
        List<Integer> list = new ArrayList<>();
        for (int s : ss) {
            list.add(s);
        }
        System.out.println(list.size());
        Set<Integer> statusSet = Arrays.stream(ss).boxed().collect(Collectors.toSet());
        System.out.println(statusSet.size());
    }

    @Test
    public void testAbs() {
        int a = -12345;
        int j = a >> 31;
        System.out.println((a ^ j) - j);
    }

    @Test
    public void testBase64() {
//        String url = "http://cs1h.wending.yuedu.163.com/route/wap2pay.do?page=sharePayment&rechargeUuid=7109ed4d-a7a2-409e-8b1e-8e67da29dda0";
//
//        String encodedUrl = Base64.getUrlEncoder().encodeToString(url.getBytes());
//        System.out.println(encodedUrl);

        String ss = "aHR0cDovL2NzeWh6LnByZS55dW55dWVkdTEwLmNuL3JvdXRlL3dhcDJwYXkuZG8_cGFnZT1zaGFyZVBheW1lbnQmcmVjaGFyZ2VVdWlkPWYxMWJkZDcwLTEzZDktNGQ4My1iOTUwLTcxYWRiYzVhMzM0YQ==";

        byte[] decodedBytes = Base64.getUrlDecoder().decode(ss);
        System.out.println(new String(decodedBytes));



    }

    @Test
    public void testaaaaaa() {
//        List<Integer> dd = Lists.newArrayList();
//        for (int i = 1; i <= 1000; i++) {
//            dd.add(i);
//        }
//        dd.parallelStream().forEach(i -> {
//            if (i % 2 == 1) {
//                throw new RuntimeException("error");
//            }
//            System.out.println(i);
//        });
        Object s = null;
        System.out.println((String) s);
    }

    @Test
    public void testChannelSign() {
        String secretkey = "VjVIGRX5YgJCGQjC";
        String params = "consumerkey=11790115&timestamp=1604647583669&sign=db044c2c123049115cd6e0910105cf73";
        UriParams uriParams = UriUtil.parseQueryString(params, true);
        Map<String, String> map = uriParams.asSingleValueMap();
        System.out.println(map);

        List<String> paramNameList = map.keySet().stream()
                .filter(item -> !item.equalsIgnoreCase("sign"))
                .collect(Collectors.toList());
        paramNameList.add("secretkey");
        Collections.sort(paramNameList);
        StringBuilder sb = new StringBuilder();
        for (String p : paramNameList) {
            if(p.equals("secretkey")) {
                sb.append(p).append("=").append(secretkey);
                continue;
            }
            sb.append(p).append("=").append(map.get(p));
        }
        System.out.println(sb.toString());
        String correctSign = DigestUtils.md5Hex(sb.toString());
        String paramSign = map.get("sign");
        System.out.println(correctSign);
        System.out.println("check:" + correctSign.equals(paramSign));
    }

    @Test
    public void testLinkedHashMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("c", "cc");
        map.put("d", "dd");
        map.put("a", "aa");
        map.put("b", "bb");
//        System.out.println(map.keySet().iterator().next());
        System.out.println(new ArrayList<>(map.keySet()));
//        Map<String, Object> m = Maps.newHashMap();
//        m.put("dd", 1);
//        m.put("aa", "cc");
//        System.out.println(m);
//        System.out.println(JSONObject.toJSONString(m));
    }

    @Test
    public void testJSON() {
//        Object[] p = new Object[2];
//        p[0] = new Person(1L, "dd", 3);
//        p[1] = "string1";
//        System.out.println(JSON.toJSONString(p));

        Man o = new Man();
        o.setAge(5);
        o.setType("man1");
        Human human1 = o;

        Home home = new Home();
        home.setName("home1");
        home.setHuman(human1);
        String s = JSONObject.toJSONString(home);
        System.out.println(s);

        Home hh = JSONObject.parseObject(s, Home.class);
        System.out.println(hh);
    }

    @Test
    public void testSwitch() {
        int i = 0;
        switch (i) {
            case 0 : {
                System.out.println("0");
                break;
            }
            case 1:
                System.out.println("1");
                break;
            case 2:
                System.out.println("2");
                break;
            default:
        }
    }

    @Test
    public void testJdkSerial() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C:\\Users\\ctl\\Desktop\\pp.txt"));
        OldPerson pp = new OldPerson("成成", 22);
        oos.writeObject(pp);
    }

    @Test
    public void testJdkSerial2() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\Users\\ctl\\Desktop\\pp.txt"));
        OldPerson pp = (OldPerson) ois.readObject();
        System.out.println(pp);
    }

    @Test
    public void testrn() {
        String s = "第1章\n" +
                "时代？";
        String s1 = s.replaceAll("[\\n\\r]", "");
        System.out.println(s1);
    }

    @Test
    public void testLimit() {
        List<Integer> list = Lists.newArrayList(1, 2, 3);
        List<Integer> collect = list.stream().limit(4).collect(Collectors.toList());
        System.out.println(collect);
    }

    @Test
    public void testSchedule() throws InterruptedException {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        CountDownLatch latch = new CountDownLatch(1);
        boolean[] initialized = new boolean[1];
//        initialized[0] = false;

        System.out.println("start");

        scheduledThreadPool.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(5000L);
            } catch (InterruptedException e) {
            }
            System.out.println(Thread.currentThread().getName() + " thread end");

            if (!initialized[0]) {
                latch.countDown();
                initialized[0] = true;
            }

        }, 0,10, TimeUnit.SECONDS);

        latch.await();
        System.out.println("main");

//        try {
//            Thread.sleep(500000L);
//        } catch (InterruptedException e) {
//        }
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(String.format("1:%s, 2:%s", "a", null));
        Map<String, Integer> map = Maps.newHashMap();
        map.put("k1", 1);
        map.put("k2", 2);
        map.put("k3", 3);
        Stream<Integer> stream = map.values().stream();
        List<Integer> list = stream.map(i -> i + 1).collect(Collectors.toList());
        System.out.println(list);
        stream.forEach(System.out::println);
    }

    @Test
    public void testMe() throws JsonProcessingException {
        System.out.println(Arrays.toString(new String[]{"a", "b"}));
        System.out.println(new ObjectMapper().writeValueAsString(new String[]{"a", "b"}));
//        String s1 = null;
//        String s2 = "";
//        Long n1 = null;
//        Integer n2 = 3;
//        System.out.println(isAnyInvalid(s1, s2, n1));
//        System.out.println(isAnyValid(s1, s2, n1, n2));
    }

    private boolean isAnyInvalid(Object... objs) {
        if (objs.length == 0) {
            throw new RuntimeException("isAnyInvalid parameter length is zero");
        }
        for (Object obj : objs)
            if (!isValid(obj))
                return true;
        return false;
    }

    private boolean isAnyValid(Object... objs) {
        if (objs.length == 0) {
            throw new RuntimeException("isAnyValid parameter length is zero");
        }
        for (Object obj : objs)
            if (isValid(obj))
                return true;
        return false;
    }

    private boolean isValid(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            return StringUtils.isNotBlank((String) obj);
        } else if (obj instanceof Long) {
            return NumberUtils.existAndGt0((Long) obj);
        } else if (obj instanceof Integer) {
            return NumberUtils.existAndGt0((Integer) obj);
        } else {
            throw new RuntimeException("isValid just support String or Long or Integer");
        }
    }



}
