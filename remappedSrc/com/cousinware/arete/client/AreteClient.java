package com.cousinware.arete.client;

import com.cousinware.arete.managers.*;
import com.cousinware.arete.utils.texture.Capes;
import com.cousinware.arete.utils.Member;
import com.cousinware.arete.utils.Users;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.fontgui.FontGui;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.gamegui.GameGui;
import com.cousinware.arete.utils.guis.clickgui.defaultguis.maingui.AreteGui;
import com.cousinware.arete.utils.file.ConfigFolder;
import com.cousinware.arete.utils.guis.jguis.FailedHwidCheckGui;
import com.cousinware.arete.utils.threads.MemberUpdateThread;
import com.google.common.eventbus.EventBus;
import com.sun.management.OperatingSystemMXBean;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class AreteClient implements ModInitializer {


    public static String CLIENTNAME = "Arete";
    //TODO encrypt passwords with sha256 key

    public static EventBus eventBus;
    public static String title = "";

    public static ArrayList<String> hwids = new ArrayList<>();

    public static ThreadManager threadManager;
    public static ArrayList<String> userList;

    ConfigFolder configFolder;
    public static OS os;
    public static Capes capes;
    public static CommandManager commandManager;
    public static ChatManager chatManager;
    public static FriendManager friendManager;
    public static SettingManager settingManager;
    public static ModuleManager moduleManager;
    public static FontManager fontManager;
    public static AreteGui areteGui;
    public static GameGui gameGui;
    public static FontGui fontGui;
    public static KeybindManager keyboardManager;
    public static final TextRenderer[] tr = new TextRenderer[1];
    public static GameManager gameManager;
    public static RotationManager rotationManager;
    public static RotationManager2 rotationManager2;
    public static PacketManager packetManager;
    public static String currentHWID = String.valueOf(Runtime.getRuntime().availableProcessors() + ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize());
    String osName;
    public static Users users;
    public static Set<String> validIgns = new CopyOnWriteArraySet<>();
    public static List<Member> members = new CopyOnWriteArrayList<>();
    public static Thread memberUpdateThread;




    @Override
    public void onInitialize() {
        threadManager = new ThreadManager();
        osCheck();

        //TODO TEST TEST


        //attempts to login to DB and see if user has valid permission
        //login();
        //checks to see if used device has correct HWID
        //hwidCheck();
        eventBus = new EventBus("AreteEventBus");
        //
        users = new Users();
        userList = users.getUsers();

        chatManager = new ChatManager();
        commandManager = new CommandManager();
        friendManager = new FriendManager();
        settingManager = new SettingManager();
        rotationManager2 = new RotationManager2();
        moduleManager = new ModuleManager();
        fontManager = new FontManager();
        keyboardManager = new KeybindManager();
        gameManager = new GameManager();
        rotationManager = new RotationManager();
        packetManager = new PacketManager();

        try {
            configFolder = new ConfigFolder();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        initThreads();
        Runtime.getRuntime().addShutdownHook(new ConfigFolder.ShutDown());
        initClickGuis();

        System.out.println(hwids);


    }

    public static Identifier getFileLocation(String path) {
        return Identifier.of("arete", path);
    }

    public void initThreads() {
        memberUpdateThread = new Thread(new MemberUpdateThread());
    }

    public void initClickGuis() {
        areteGui = new AreteGui();
        fontGui = new FontGui();
        gameGui = new GameGui();
    }

//    public static void initFont() {
//        vg = NanoVGGL3.nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
//        if (vg == 0) {
//            System.out.println("failed Here");
//        }
//
//
//        int font = NanoVG.nvgCreateFont(vg, "sans", "resources/assets/arete/font/arial.ttf");
//        if (font == -1) {
//            System.out.println("failed Here 2");
//            // Handle font loading error
//        }
//
//// Render text
//        NanoVG.nvgBeginFrame(vg, 1000, 1000, 100);
//        NanoVG.nvgFontFaceId(vg, font);
//        NanoVG.nvgFontSize(vg, 18.0f);
//        NanoVG.nvgText(vg, 100, 100, "Hello, world!");
//        NanoVG.nvgEndFrame(vg);
//    }

    public void hwidCheck() {
        if (!hwids.contains(currentHWID)) {
            new FailedHwidCheckGui();
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(currentHWID);
            System.out.println("Failed HWID check");
            System.exit(1);
        }
    }

    public void osCheck() {
        osName = System.getProperty("os.name");
        if (osName.charAt(0) == 'w' || osName.charAt(0) == 'W') {
            // for some reason the main mc thread runs Headless as true
            //due to this some things in java are limited ie adding something to clipboard or doing our login stuff
            //TODO support mac
            os = OS.WINDOWS;
            System.setProperty("java.awt.headless", "false");
            System.out.println("Windows Device detected enabling!");
        } else if (osName.charAt(0) == 'm' || osName.charAt(0) == 'M') {
            os = OS.MAC;
        } else {
            os = OS.LINUX;
        }
    }

//    public static void login() {
//
//        //TODO make an manager which allows us to make request to the DB
//        String data = "";
//        //if the loginthread has not been invoked this means the user already had a valid AreteVeify file, there for we read the data from said file
//        if (!LoginThread.invoked) {
//            try {
//
//                Scanner myReader = new Scanner(LoginFileHelper.userFile);
//                while (myReader.hasNextLine()) {
//                    data = myReader.nextLine();
//                }
//                myReader.close();
//            } catch (FileNotFoundException e) {
//                System.out.println("An error occurred.");
//                e.printStackTrace();
//            }
//        }
//
//        //based on previous inquires we decide how to get the user and pass
//        // reason we dont read from file 24/7 is because we update the file on closing the game
//
//        try {
//            username = LoginThread.invoked ? LoginGui.username : data.split(":")[0];
//            password = LoginThread.invoked ? LoginGui.password : data.split(":")[1];
//        } catch (Exception e) {
//            try {
//                userFolder = new LoginFileHelper();
//                login();
//            } catch (IOException z) {
//                throw new RuntimeException(z);
//            }
//        }
//
//
//        //connection string to my server
//        String connectionString = "mongodb+srv://"+ username +":" + password + "@cluster1.5sltbtu.mongodb.net/?retryWrites=true&w=majority&appName=Cluster1";
//        ServerApi serverApi = ServerApi.builder()
//                .version(ServerApiVersion.V1)
//                .build();
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(new ConnectionString(connectionString))
//                .serverApi(serverApi)
//                .build();
//        // Create a new client and connect to the server
//        try (MongoClient mongoClient = MongoClients.create(settings)) {
//            try {
//                // Send a ping to confirm a successful connection
//                MongoDatabase database = mongoClient.getDatabase("hwids");
//                database.runCommand(new Document("ping", 1));
//
//
//                Bson filter = Filters.empty();
//                MongoCollection<Document> collection = database.getCollection("hwids");
//
//                //gets all HWIDS from the DB and loads them into memory to see the machine has the right HWID
//                collection.aggregate(Arrays.asList(
//                                Aggregates.match(filter),
//                                Aggregates.group("$_id"),
//                                Aggregates.sort(Sorts.descending("_id"))))
//                        .forEach(doc -> hwids.add(doc.toString().substring(doc.toString().lastIndexOf("=") + 1, doc.toString().length() - 2)));
//
//
//                //if were hear we signed in correctly!
//            } catch (MongoException e) {
//                //! if we catch this excetption that means they failed to login so an obvious exit
//
//                try {
//                    userFolder = new LoginFileHelper();
//                    login();
//                } catch (IOException z) {
//                    throw new RuntimeException(z);
//                }
//
//
//            }
//        } catch (Exception e) {
//            System.out.println("why did u crash here?");
//        }
//
//        //TODO if were here they successfully signed in via user & pass and they passed the HWID verification test
//        //TODO so now we should check to see if there is an update so we can install the new loader and restart the game
//    }

    public enum OS {
        WINDOWS,
        MAC,
        LINUX

    }

}

