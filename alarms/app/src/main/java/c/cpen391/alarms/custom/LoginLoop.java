package c.cpen391.alarms.custom;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import c.cpen391.alarms.R;
import su.levenetc.android.textsurface.Text;
import su.levenetc.android.textsurface.TextBuilder;
import su.levenetc.android.textsurface.TextSurface;
import su.levenetc.android.textsurface.animations.Alpha;
import su.levenetc.android.textsurface.animations.AnimationsSet;
import su.levenetc.android.textsurface.animations.ChangeColor;
import su.levenetc.android.textsurface.animations.Circle;
import su.levenetc.android.textsurface.animations.Delay;
import su.levenetc.android.textsurface.animations.Loop;
import su.levenetc.android.textsurface.animations.Parallel;
import su.levenetc.android.textsurface.animations.Rotate3D;
import su.levenetc.android.textsurface.animations.Sequential;
import su.levenetc.android.textsurface.animations.ShapeReveal;
import su.levenetc.android.textsurface.animations.SideCut;
import su.levenetc.android.textsurface.animations.Slide;
import su.levenetc.android.textsurface.animations.TransSurface;
import su.levenetc.android.textsurface.contants.Align;
import su.levenetc.android.textsurface.contants.Axis;
import su.levenetc.android.textsurface.contants.Direction;
import su.levenetc.android.textsurface.contants.Pivot;
import su.levenetc.android.textsurface.contants.Side;
import su.levenetc.android.textsurface.contants.TYPE;


public class LoginLoop {
    public static void play(TextSurface textSurface, AssetManager assetManager, Context c) {

        final Typeface robotoBlack = Typeface.createFromAsset(assetManager, "fonts/AirbnbCereal-Bold.ttf");
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTypeface(robotoBlack);

        Text title = TextBuilder
                .create("Sleep App")
                .setPaint(paint)
                .setSize(60)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(c, R.color.dark_gray))
                .setPosition(Align.SURFACE_CENTER).build();

        Text track = TextBuilder
                .create("Track")
                .setPaint(paint)
                .setSize(25)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(c, R.color.goog_green))
                .setPosition(Align.BOTTOM_OF, title).build();

        Text sleepTrends = TextBuilder
                .create(" your sleep trends")
                .setPaint(paint)
                .setSize(25)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(c, R.color.mid_gray))
                .setPosition(Align.RIGHT_OF, track).build();

        Text play = TextBuilder
                .create("Play")
                .setPaint(paint)
                .setSize(25)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(c, R.color.goog_yellow))
                .setPosition(Align.BOTTOM_OF, track).build();

        Text funGames = TextBuilder
                .create(" exciting games")
                .setPaint(paint)
                .setSize(25)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(c, R.color.mid_gray))
                .setPosition(Align.RIGHT_OF, play).build();

        Text compete = TextBuilder
                .create("Compete")
                .setPaint(paint)
                .setSize(25)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(c, R.color.goog_red))
                .setPosition(Align.BOTTOM_OF, play).build();

        Text againstFriends = TextBuilder
                .create(" against friends")
                .setPaint(paint)
                .setSize(25)
                .setAlpha(0)
                .setColor(ContextCompat.getColor(c, R.color.mid_gray))
                .setPosition(Align.RIGHT_OF, compete).build();

        final int flash = 1500;

        textSurface.play(
                new Loop(
                        new AnimationsSet(TYPE.PARALLEL,
                                ShapeReveal.create(title, flash, SideCut.hide(Side.LEFT), false),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(flash / 4), ShapeReveal.create(title, flash, SideCut.show(Side.LEFT), false))
                        ),
                        new AnimationsSet(TYPE.PARALLEL,
                                new Sequential(ShapeReveal.create(track, 500, Circle.show(Side.CENTER, Direction.OUT), false))
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                new Sequential(ShapeReveal.create(sleepTrends, 500, Circle.show(Side.CENTER, Direction.OUT), false))
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                new Sequential(ShapeReveal.create(play, 500, Circle.show(Side.CENTER, Direction.OUT), false))
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                new Sequential(ShapeReveal.create(funGames, 500, Circle.show(Side.CENTER, Direction.OUT), false))
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                new Sequential(ShapeReveal.create(compete, 500, Circle.show(Side.CENTER, Direction.OUT), false))
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                new Sequential(ShapeReveal.create(againstFriends, 500, Circle.show(Side.CENTER, Direction.OUT), false))
                        ),
                        Delay.duration(500),
                        new AnimationsSet(TYPE.PARALLEL,
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(500), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(compete, 700), ShapeReveal.create(compete, 1000, SideCut.hide(Side.LEFT), true))),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(500), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(againstFriends, 700), ShapeReveal.create(againstFriends, 1000, SideCut.hide(Side.LEFT), true))),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(1000), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(play, 700), ShapeReveal.create(play, 1000, SideCut.hide(Side.LEFT), true))),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(1000), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(funGames, 700), ShapeReveal.create(funGames, 1000, SideCut.hide(Side.LEFT), true))),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(1500), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(track, 700), ShapeReveal.create(track, 1000, SideCut.hide(Side.LEFT), true))),
                                new AnimationsSet(TYPE.SEQUENTIAL, Delay.duration(1500), new AnimationsSet(TYPE.PARALLEL, Alpha.hide(sleepTrends, 700), ShapeReveal.create(sleepTrends, 1000, SideCut.hide(Side.LEFT), true))),
                                new TransSurface(4000, title, Pivot.CENTER)
                        )
                )
        );
    }
}