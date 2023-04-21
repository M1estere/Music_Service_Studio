package com.example.music_service.models.data;

import android.app.Activity;

import com.example.music_service.models.data.SongsProps;

public class DataLoader {

    public static final String BASE_URL = "https://drive.google.com/uc?id=";

    public static void fillSongsFromDrive(Activity activity) {
        addSong("1xxp53U3PHZljo7U6zpNdamZ-qWNYR0aA", "alive.mp3");
        addSong("1ZqMaDkZuCU7fr4QUHmp0v-V2kpRk3xYZ", "ancients.mp3");
        addSong("1794d4RMX5VHbPzAvOALQeYzbt12_LhX0", "animals.mp3");
        addSong("1JyNk6bTQDLJBppvTbOpWqPPEZPSihKMZ", "ashes_of_dreams.mp3");
        addSong("13GMzHPT6yyNhogx2tdYl-6PtdDrV0-hF", "bad.mp3");

        addSong("1ieBtOEu2WgtHPJHqEeamH8_tFon6uOll", "beat_it.mp3");
        addSong("1wMcM0NjsMN4XOA6xY7DM6kBe3yxH-F3M", "believer.mp3");
        addSong("1U4G2BIjGNQKMmMJzn2DC8eKQqHV_Gm_L", "billie_jean.mp3");
        addSong("1SMVRNq8tAZJHtx8mqkMJlCGEecM5ROXK", "break_through_it_all.mp3");
        addSong("1LuUaq2X0pCa5Hfb-WSKliDXcMgX44L09", "circles.mp3");

        addSong("114bWXbdp4T40Xgk7IIXYN6zOVeQlQB14", "cold_heart.mp3");
        addSong("1hDkkLNPeKieM66KlyElrUUvwj7aKyJmO", "easier.mp3");
        addSong("1oJqnbt-mGY7_iVDYNBI0cfMKz1kuPi60", "electrical_storm.mp3");
        addSong("1SLalSZKfkqCpND7rIF_gmPhPybAy8Arz", "endless_possibility.mp3");
        addSong("1arrsJ77uctLSW1MuCdMB_poRRw-DuJAz", "feel_good_inc.mp3");

        addSong("10Bjtx4eaDnEjqwHAuxEK_8D0nG2_ogg4", "get_lucky.mp3");
        addSong("1XhGZmOqf-JhOL_3VH1-Bt44ID7OdmHsw", "grandmother.mp3");
        addSong("17ytq5D8_el-BxelW8TgAfwOpvMWOtdB7", "guy_exe.mp3");
        addSong("1srLlJsB0vi7O871Dg1s3o5stn9pAeqXE", "his_world.mp3");
        addSong("1ZWEuC-vI8Wr3OykorTHR7yLbkiHsxL7G", "i_feel_it_coming.mp3");

        addSong("1VuvQSmDnRlb79XBUb1sX3U1Yfe89Pt_f", "in_your_eyes.mp3");
        addSong("1UmIF5hAVHNkUTIHMegfCzDwYU3XiBupS", "infinite.mp3");
        addSong("1N2mFLS-Ias2F17o_-YEQ8uaeLkLIoeky", "last_christmas.mp3");
        addSong("1k7YUYcXGdP6i8HAtFRfTomQ3qPMLkpme", "lost_in_japan.mp3");
        addSong("1kICFte8iYX7cfnV2WIGrMmY53DmEfOjm", "makes_me_wonder.mp3");

        SongsProps.checkCoversArtists(activity);
    }

    public static void addSong(String uri, String title) {
        SongsProps.uris.add(BASE_URL + uri);
        SongsProps.songs.add(title);
    }

}
