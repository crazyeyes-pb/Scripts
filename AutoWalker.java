import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSTile;
import org.rsbot.script.wrappers.Web;

@ScriptManifest(authors = { "SpeedWing" }, website = "http://www.powerbot.org/vb/showthread.php?t=734531", name = "AutoWalking Simplified", version = 1.04, description = "Simply type your Destination!")
public class AutoWalker extends Script implements PaintListener, MouseListener {

	Location CURRENT = null;
	Location TEMP = null;
	Locations locations = new Locations();
	Web WP = null;
	RSTile[] PATH = null;
	boolean SETTING_LOCATION = true;
	boolean REMOVED_TEXT = false;

	public void drawLocations(Graphics g) {
		int x = 10, y = 5, width = 0;
		g.setColor(new Color(255, 255, 255));
		g.setFont(new Font("Verdana", Font.PLAIN, 10));
		for (int i = 0; i < locations.ALL_LOCATIONS.length; i++) {
			drawStringWithShadow(locations.ALL_LOCATIONS[i].NAME, x, y += 12, g);
			int tempWidth = g.getFontMetrics().stringWidth(
					locations.ALL_LOCATIONS[i].NAME);
			width = ((tempWidth > width) ? tempWidth : width);
			if (i < locations.ALL_LOCATIONS.length - 1
					&& (y > game.getHeight() - 60)) {
				y = 5;
				x += width + 16;
				width = 0;

				g.setColor(new Color(255, 255, 255, 120));
				g.drawLine(x - 8, y, x - 8, game.getHeight() - 55);
				g.setColor(new Color(255, 255, 255));
			}
		}
	}

	public void drawMouse(Graphics g) {
		Point m = mouse.getLocation();
		long mpt = System.currentTimeMillis() - mouse.getPressTime();
		if (mpt < 80 && mpt > 0 && mpt != -1)
			drawSqaure(g, m, 7);
		else if (mpt >= 80 && mpt < 160)
			drawSqaure(g, m, 6);
		else if (mpt >= 160 && mpt < 240)
			drawSqaure(g, m, 5);
		else if (mpt >= 240 && mpt < 320)
			drawSqaure(g, m, 4);
		else if (mpt >= 320 && mpt < 400)
			drawSqaure(g, m, 3);
		else if (mpt >= 480 && mpt < 560)
			drawSqaure(g, m, 2);
		else if (mpt >= 560 && mpt < 640)
			drawSqaure(g, m, 2);
		else if (mpt >= 640 && mpt < 720)
			drawSqaure(g, m, 2);
		else if (mpt >= 720 && mpt < 800)
			drawSqaure(g, m, 3);
		else if (mpt >= 800 && mpt < 880)
			drawSqaure(g, m, 4);
		else if (mpt >= 880 && mpt < 960)
			drawSqaure(g, m, 5);
		else if (mpt >= 960 && mpt < 1040)
			drawSqaure(g, m, 6);
		if (mpt >= 1040)
			drawSqaure(g, m, 7);
	}

	public void drawSqaure(Graphics g, Point p, int size) {
		int size2 = size - 1;
		Polygon poly = new Polygon(), poly2 = new Polygon();
		poly.addPoint(p.x - size, p.y);
		poly.addPoint(p.x, p.y + size);
		poly.addPoint(p.x + size, p.y);
		poly.addPoint(p.x, p.y - size);
		poly2.addPoint(p.x - size2, p.y);
		poly2.addPoint(p.x, p.y + size2 + 1);
		poly2.addPoint(p.x + size2 + 1, p.y);
		poly2.addPoint(p.x, p.y - size2);

		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(255, 0, 0, 175));
		g.fillPolygon(poly2);
		g.setColor(Color.black);
		g.drawPolygon(poly);
		int line = 5;
		g.setColor(Color.white);
		g.drawLine(poly.xpoints[0], poly.ypoints[0], poly.xpoints[0] - line,
				poly.ypoints[0]);
		g.drawLine(poly.xpoints[2], poly.ypoints[0], poly.xpoints[2] + line,
				poly.ypoints[0]);
		g.drawLine(poly.xpoints[1], poly.ypoints[1], poly.xpoints[1],
				poly.ypoints[1] + line);
		g.drawLine(poly.xpoints[3], poly.ypoints[3], poly.xpoints[3],
				poly.ypoints[3] - line);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	public void drawStartButton(Graphics g) {
		g.setColor(new Color(0, 255, 0, 190));
		g.fillRoundRect(340, game.getHeight() - 25, 170, 22, 5, 5);
		g.setColor(new Color(255, 255, 255));
		g.drawRoundRect(340, game.getHeight() - 25, 170, 22, 5, 5);

		g.setColor(new Color(255, 255, 255));
		g.setFont(new Font("Verdana", Font.BOLD, 12));
		drawStringWithShadow("Click here to Start", 365, game.getHeight() - 10,
				g);
	}

	public void drawStringWithShadow(final String text, final int x,
			final int y, final Graphics g) {
		final Color col = g.getColor();
		g.setColor(new Color(0, 0, 0));
		g.drawString(text, x + 1, y + 1);
		g.setColor(col);
		g.drawString(text, x, y);
	}

	public String getTextBox() {
		RSInterface chatBox = interfaces.get(137);
		for (int i = 0; i < 280; i++) {
			String text = chatBox.getComponent(i).getText();
			if (!text.isEmpty()
					&& text.contains(getMyPlayer().getName() + "<img"))
				return text;
		}
		return "";
	}

	public class Location {
		RSTile TILE;
		String NAME;

		public Location(RSTile tile, String name) {
			this.TILE = tile;
			this.NAME = name;
		}
	}

	public class Locations {
		Location DRAYNOR_VILLAGE = new Location(new RSTile(3094, 3244),
				"Draynor Village");
		Location GRAND_EXCHANGE = new Location(new RSTile(3167, 3489),
				"Grand Exchange");
		Location VARROCK_EAST_BANK = new Location(new RSTile(3253, 3422),
				"Varrock East Bank");
		Location VARROCK_WEST_BANK = new Location(new RSTile(3186, 3438),
				"Varrock West Bank");
		Location LUMBRIDGE = new Location(new RSTile(3222, 3220), "Lumbridge");
		Location BARBARIAN_VILLAGE = new Location(new RSTile(3081, 3416),
				"Barbarian Village");
		Location EDGEVILLE = new Location(new RSTile(3086, 3493), "Edgeville");
		Location FALADOR_EAST_BANK = new Location(new RSTile(3012, 3359),
				"Falador East Bank");
		Location FALADOR_WEST_BANK = new Location(new RSTile(2946, 3374),
				"Falador West Bank");
		Location RIMMINGTON = new Location(new RSTile(2956, 3211), "Rimmington");
		Location PORT_SARIM = new Location(new RSTile(3019, 3254), "Port Sarim");
		Location AL_KHARID_BANK = new Location(new RSTile(3272, 3168),
				"Al Kharid Bank");
		Location SHANTAY_PASS = new Location(new RSTile(3305, 3124),
				"Shantay Pass");
		Location FIRE_ALTAR = new Location(new RSTile(3312, 3253), "Fire Altar");
		Location EARTH_ALTAR = new Location(new RSTile(3304, 3473),
				"Earth Altar");
		Location AIR_ALTAR = new Location(new RSTile(3129, 3405), "Air Altar");
		Location WATER_ALTAR = new Location(new RSTile(3183, 3166),
				"Water Altar");
		Location MIND_ALTAR = new Location(new RSTile(2982, 3512), "Mind Altar");
		Location BODY_ALTAR = new Location(new RSTile(3054, 3443), "Body Altar");
		Location DUELING_ARENA = new Location(new RSTile(3366, 3266),
				"Dueling Arena");
		Location VARROCK_EAST_MINE = new Location(new RSTile(3286, 3365),
				"Varrock East Mine");
		Location VARROCK_WEST_MINE = new Location(new RSTile(3182, 3371),
				"Varrock West Mine");
		Location VARROCK_CENTER = new Location(new RSTile(3213, 3429),
				"Varrock Center");
		Location VARROCK_PALACE = new Location(new RSTile(3214, 3462),
				"Varrock Palace");
		Location COOKING_GUILD = new Location(new RSTile(3143, 3443),
				"Cooking Guild");
		Location MONASTERY = new Location(new RSTile(3051, 3491), "Monastery");
		Location GOBLIN_VILLAGE = new Location(new RSTile(2956, 3495),
				"Goblin Village");
		Location FALADOR_CENTER = new Location(new RSTile(2965, 3380),
				"Falador Center");
		Location WHITE_KNIGHT_CASTLE = new Location(new RSTile(2970, 3344),
				"White Knight Castle");
		Location MINING_GUILD = new Location(new RSTile(3020, 3338),
				"Mining Guild");
		Location RIMMINGTON_YEWS = new Location(new RSTile(2931, 3229),
				"Rimmington Yews");
		Location MELZARS_MAZE = new Location(new RSTile(2945, 3240),
				"Melzar's Maze");
		Location RIMMINGTON_MINE = new Location(new RSTile(2978, 3241),
				"Rimmington Mine");
		Location WIZARDS_TOWER = new Location(new RSTile(3113, 3169),
				"Wizard's Tower");
		Location SEERS_BANK = new Location(new RSTile(2724, 3491), "Seers Bank");
		Location CATHERBY_BANK = new Location(new RSTile(2808, 3441),
				"Catherby Bank");
		Location RELLEKKA = new Location(new RSTile(2661, 3646), "Rellekka");
		Location DRAYNOR_MANOR = new Location(new RSTile(3112, 3324),
				"Draynor Manor");
		Location YANILLE_BANK = new Location(new RSTile(2612, 3092),
				"Yanille Bank");
		Location CRAFTING_GUILD = new Location(new RSTile(2923, 3296),
				"Crafting Guild");
		Location DWARVEN_MINE = new Location(new RSTile(3016, 3447),
				"Dwarven Mine");
		Location HEROES_GUILD = new Location(new RSTile(2904, 3510),
				"Heroes' Guild");
		Location CAMELOT_CASTLE = new Location(new RSTile(2757, 3479),
				"Camelot Castle");
		Location FISHING_GUILD = new Location(new RSTile(2614, 3383),
				"Fishing Guild");
		Location LEGENDS_GUILD = new Location(new RSTile(2729, 3347),
				"Legends' Guild");
		Location RANGING_GUILD = new Location(new RSTile(2656, 3439),
				"Ranging Guild");
		Location BARBARIAN_ASSUALT = new Location(new RSTile(2518, 3570),
				"Barbarian Assualt");
		Location GNOME_STRONGHOLD = new Location(new RSTile(2460, 3381),
				"Gnome Stronghold");
		Location ARDOUGNE_MARKET_BANK = new Location(new RSTile(2653, 3283),
				"Ardougne Market Bank");
		Location ARDOUGNE_NORTH_BANK = new Location(new RSTile(2616, 3333),
				"Ardougne North Bank");
		Location ARDOUGNE_MARKET = new Location(new RSTile(2663, 3302),
				"Ardougne Market");
		Location CASTLE_WARS = new Location(new RSTile(2443, 3089),
				"Castle Wars");
		Location AL_KHARID_MINE = new Location(new RSTile(3297, 3283),
				"Al Kharid Mine");
		Location PISCATORIS_MINE = new Location(new RSTile(2345, 3643),
				"Piscatoris Mine");
		Location TAVERLEY = new Location(new RSTile(2896, 3455), "Taverley");
		Location CLOCKTOWER = new Location(new RSTile(2578, 3245), "Clocktower");
		Location LUMBRIDGE_MILL = new Location(new RSTile(3166, 3298),
				"Lumbridge Mill");
		Location CANIFIS = new Location(new RSTile(3495, 3482), "Canifis");
		Location LUMBRIDGE_SWAMP = new Location(new RSTile(3199, 3178),
				"Lumbridge Swamp");
		Location SOUL_WARS_PORTAL = new Location(new RSTile(3080, 3475),
				"Soul Wars Portal");
		Location CHAMPIONS_GUILD = new Location(new RSTile(3191, 3367),
				"Champion's Guild");

		Location[] ALL_LOCATIONS = { AIR_ALTAR, AL_KHARID_BANK, AL_KHARID_MINE,
				ARDOUGNE_MARKET, ARDOUGNE_MARKET_BANK, ARDOUGNE_NORTH_BANK,
				BARBARIAN_ASSUALT, BARBARIAN_VILLAGE, BODY_ALTAR,
				CAMELOT_CASTLE, CANIFIS, CASTLE_WARS, CATHERBY_BANK,
				CHAMPIONS_GUILD, CLOCKTOWER, COOKING_GUILD, CRAFTING_GUILD,
				DRAYNOR_MANOR, DRAYNOR_VILLAGE, DUELING_ARENA, DWARVEN_MINE,
				EARTH_ALTAR, EDGEVILLE, FALADOR_CENTER, FALADOR_EAST_BANK,
				FALADOR_WEST_BANK, FIRE_ALTAR, FISHING_GUILD, GNOME_STRONGHOLD,
				GOBLIN_VILLAGE, GRAND_EXCHANGE, HEROES_GUILD, LEGENDS_GUILD,
				LUMBRIDGE, LUMBRIDGE_MILL, LUMBRIDGE_SWAMP, MELZARS_MAZE,
				MIND_ALTAR, MINING_GUILD, MONASTERY, PISCATORIS_MINE,
				PORT_SARIM, RANGING_GUILD, RELLEKKA, RIMMINGTON,
				RIMMINGTON_MINE, RIMMINGTON_YEWS, SEERS_BANK, SHANTAY_PASS,
				SOUL_WARS_PORTAL, TAVERLEY, VARROCK_CENTER, VARROCK_EAST_BANK,
				VARROCK_EAST_MINE, VARROCK_PALACE, VARROCK_WEST_BANK,
				VARROCK_WEST_MINE, WATER_ALTAR, WHITE_KNIGHT_CASTLE,
				WIZARDS_TOWER, YANILLE_BANK };
	}

	@Override
	public int loop() {
		if (SETTING_LOCATION) {
			String text = getTextBox();
			text = text.substring(text.indexOf(">") + 1);
			text = text.substring(text.indexOf(">") + 1).toLowerCase();
			if (!text.equals(""))
				for (int i = 0; i < locations.ALL_LOCATIONS.length; i++) {
					Location temp = locations.ALL_LOCATIONS[i];
					if (temp.NAME.toLowerCase().contains(text)) {
						TEMP = temp;
						break;
					}
				}
			else
				TEMP = null;
		} else {
			if (!REMOVED_TEXT) {
				int length = getTextBox().length();
				for (int i = 0; i < length; i++) {
					keyboard.pressKey((char) KeyEvent.VK_BACK_SPACE);
					sleep(50);
				}
				REMOVED_TEXT = true;
			}

			if (PATH == null) {
				try {
					WP = walking.getWebPath(CURRENT.TILE);
					PATH = WP.path.getTiles();
				} catch (Exception e) {
					log.warning("The Walking web was unable to generate a path.");
					stopScript();
				}
			}

			if (!WP.atDestination())
				WP.traverse();
			else {
				log.warning("- " + CURRENT.NAME + " - reached.");
				stopScript();
			}
		}
		return SETTING_LOCATION ? 50 : 1000;
	}

	@Override
	@SuppressWarnings("static-access")
	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == e.BUTTON1)
			if (SETTING_LOCATION) {
				Point p = e.getPoint();
				int x = p.x, y = p.y;
				if (x > 340 && x < 500 && y > game.getHeight() - 25
						&& y < game.getHeight() - 3) {
					if (TEMP == null) {
						log.warning("Type a location.");
						return;
					}
					CURRENT = TEMP;
					log.warning("Destination: " + CURRENT.NAME);
					SETTING_LOCATION = false;
				}
			}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRepaint(Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		if (SETTING_LOCATION) {
			// filling everything except the chat line
			g.setColor(new Color(0, 0, 0, 200));
			int gameWidth = game.getWidth(), gameHeight = game.getHeight();

			// top part
			g.fillRect(0, 0, gameWidth, gameHeight - 44);
			// bottom part
			g.fillRect(0, gameHeight - 30, gameWidth, 30);
			// right middle part
			g.fillRect(511, gameHeight - 44, gameWidth - 512, 14);
			// left middle part
			g.fillRect(0, gameHeight - 44, 7, 14);

			drawStartButton(g);
			drawLocations(g);

			// drawing the result of the search action.
			g.setColor(new Color(255, 0, 0));
			g.setFont(new Font("Verdana", Font.BOLD, 12));
			drawStringWithShadow("Found location: "
					+ ((TEMP == null) ? "None" : TEMP.NAME), 8,
					gameHeight - 11, g);

		} else {
			drawMouse(g);

			for (RSTile t : PATH) {
				Point p = calc.tileToMinimap(t);
				if (p.x != -1) {
					g.setColor(new Color(255, 255, 255));
					g.fillRect(p.x - 1, p.y - 1, 3, 3);
					g.setColor(new Color(0, 255, 0));
					g.drawRect(p.x - 1, p.y - 1, 3, 3);
				}
			}
		}
	}

	@Override
	public boolean onStart() {
		return true;
	}
}