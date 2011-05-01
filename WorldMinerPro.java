import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Game;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Filter;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSObjectDef;
import org.rsbot.script.wrappers.RSTile;
import org.rsbot.script.wrappers.RSTilePath;

@ScriptManifest(authors = "Brian", website = "http://www.powerbot.org/vb/showthread.php?t=641161", keywords = "Mining", name = "WorldMinerPro", version = 4.5, description = "Multiple Location Miner")
public class WorldMinerPro extends Script implements PaintListener,
		MessageListener {
	private minegui minegui;
	/**
	 * Tiles
	 */
	// public RSTile[] toBank;
	// public RSTile[] toMine;
	public RSTilePath toBank;
	public RSTilePath toMine;
	public RSTile BankTile;
	public RSTile MineTile;
	public RSTile FixGate = new RSTile(3045, 9742);
	public RSArea guildarea = new RSArea(new RSTile(3055, 9757), new RSTile(
			3026, 9732));
	/**
	 * Objects
	 */
	public RSObject Rock;
	/**
	 * Integers
	 */
	int countpaint;
	int AntiBan;
	int SleepTime;
	int MouseSpeed;
	int[] Rocks;
	int OreMined;
	int GemsMined;
	int profit;
	int coalprice;
	int copperprice;
	int silverprice;
	int ironprice;
	int clayprice;
	int tinprice;
	int startexp;
	int xpGained;
	int startlvl;
	int timebanked;
	int pickID[] = { 1265, 1269, 1267, 1273, 1271, 1275, 15259, 15261 };
	int[] goladdup = { 6226 };
	int[] goladdown = { 2113 };
	int[] thebankid = { 11402, 11758, 11402, 2213, 35647, 26972 };
	int[] cross = { 14504 };
	Color thecoller;

	/**
	 * Strings
	 */

	/**
	 * Booleans
	 */
	boolean PowerMine;
	boolean Start = false;
	boolean Settings = false;
	boolean MineGuild = false;
	boolean HidePaint = false;
	boolean wildi = false;
	boolean paint = true;
	boolean rest = false;
	boolean safepaint = false;
	/**
	 * Others
	 */
	BufferedImage normal = null;
	BufferedImage clicked = null;
	BufferedImage cape = null;
	long startTime = System.currentTimeMillis();

	@Override
	public boolean onStart() {
		log("Welcome to WorldMinerPro 4");
		minegui = new minegui();
		minegui.setVisible(true);
		startlvl = skills.getCurrentLevel(Skills.MINING);
		startexp = skills.getCurrentExp(Skills.MINING);

		// coalprice = grandExchange.getMarketPrice(453);
		// copperprice = grandExchange.getMarketPrice(436);
		// ironprice = grandExchange.getMarketPrice(440);
		// tinprice = grandExchange.getMarketPrice(438);
		// silverprice = grandExchange.getMarketPrice(442);
		// clayprice = grandExchange.getMarketPrice(434);
		return true;
	}

	@Override
	public void messageReceived(MessageEvent e) {
		String svrmsg = e.getMessage();
		if (svrmsg.contains("iron")) {
			OreMined = OreMined + 1;
			// profit = ironprice * OreMined;
		}
		if (svrmsg.contains("tin")) {
			OreMined = OreMined + 1;
			// profit = tinprice * OreMined;
		}
		if (svrmsg.contains("copper")) {
			OreMined = OreMined + 1;
			// profit = copperprice * OreMined;
		}
		if (svrmsg.contains("coal")) {
			OreMined = OreMined + 1;
			// profit = coalprice * OreMined;
		}
		if (svrmsg.contains("silver")) {
			OreMined = OreMined + 1;
			// profit = silverprice * OreMined;
		}
		if (svrmsg.contains("gold")) {
			OreMined = OreMined + 1;
		}
		if (svrmsg.contains("clay")) {
			// profit = clayprice * OreMined;
			OreMined = OreMined + 1;
		}
		if (svrmsg.contains("just found")) {
			GemsMined = GemsMined + 1;
		}
	}

	@Override
	public int loop() {
		if (Start) {
			if (Settings) {
				mouse.setSpeed(MouseSpeed);
				camera.setPitch(random(90, 100));
				Settings = false;
			}
			if (walking.getEnergy() > random(60, 100)) {
				walking.setRun(true);
			}
			if (rest) {
				if (walking.getEnergy() < 2) {
					walking.rest();
				}
			}
			DoAntiBan();
			if (inventory.isFull()) {
				if (PowerMine) {
					DropOre();
				} else if (!PowerMine) {
					Bank();
				}
			} else if (!inventory.isFull()) {
				Mine();

			}
		}
		return 10;

	}

	public boolean DropOre() {
		while (inventory.getCount() > 1) {
			inventory.dropAllExcept(pickID);
		}
		return true;
	}

	public void DoAntiBan() {
		AntiBan = AntiBan + 1;
		if (AntiBan == 400) {
			sleep(random(200, 300));
			int randomizer = random(1, 5);
			if (randomizer == 2) {
				camera.setAngle(random(0, 180));
				camera.setPitch(random(80, 100));
			}
			if (randomizer == 3) {
				if (getMyPlayer().getAnimation() == 624) {
					game.openTab(Game.TAB_STATS);
					mouse.move(704 + random(-30, 29), 223 + random(-13, 13));
					sleep(random(500, 1250));
					game.openTab(Game.TAB_INVENTORY);
				}
			}
			if (randomizer == 4) {
				if (getMyPlayer().getAnimation() == 624) {
					game.openTab(Game.TAB_FRIENDS);
					sleep(random(500, 1250));
					game.openTab(Game.TAB_INVENTORY);
				}
			}
			AntiBan = 0;
		}
	}

	public void Mine() {
		SleepTime = 1;
		RSObject ore = objects.getNearest(Rocks);
		if (ore != null) {
			if (calc.distanceTo(ore) < 6) {

				if (getMyPlayer().getAnimation() == 624) {
					// Already mining
					if (!onHorizontalOrVertTile(ore.getLocation())) {
						// Death Rock
						if (ore.isOnScreen()) {
							if (MineGuild) {
								if (guildarea.contains(ore.getLocation())) {

								} else {
									walking.walkTo(FixGate);
									sleep(1500);
									return;
								}
							}
							ore.doAction("Mine");
							SleepTime = 1000;

						} else {
							if (MineGuild) {
								if (guildarea.contains(ore.getLocation())) {

								} else {
									walking.walkTo(FixGate);
									sleep(1500);
									return;
								}
							}
							camera.turnTo(ore);
							ore.doAction("Mine");
							SleepTime = 1000;
						}
					}
					if (menu.contains("Mine")) {
					} else {
						RSObject NextOre = getSecondNearest(ore, Rocks);
						if (NextOre != null) {
							if (NextOre.isOnScreen()) {
								mouse.move(calc.tileToScreen(NextOre
										.getLocation()), 13, 13);
								return;
							} else {
								camera.turnTo(NextOre);
								mouse.move(calc.tileToScreen(NextOre
										.getLocation()), 13, 13);
								return;
							}
						}
					}
				}
				if (getMyPlayer().getAnimation() == -1) {
					if (ore.isOnScreen()) {
						if (getMyPlayer().isMoving()) {
						} else {
							if (MineGuild) {
								if (guildarea.contains(ore.getLocation())) {

								} else {
									walking.walkTo(FixGate);
									sleep(1500);
									return;
								}
							}
							ore.doAction("Mine");
							SleepTime = 1000;
						}
					} else {
						if (getMyPlayer().isMoving()) {
						} else {
							if (MineGuild) {
								if (guildarea.contains(ore.getLocation())) {

								} else {
									walking.walkTo(FixGate);
									sleep(1500);
									return;
								}
							}
							camera.turnTo(ore);
							ore.doAction("Mine");
							SleepTime = 1000;
						}
					}

				}
			} else {
				if (MineGuild) {
					if (guildarea.contains(ore.getLocation())) {

					} else {
						walking.walkTo(FixGate);
						sleep(1500);
						return;
					}
				}
				walking.walkTileMM(ore.getLocation());
				SleepTime = 1500;
			}
		} else {
			if (MineGuild) {
				RSObject LadderDown = objects.getNearest(goladdown);
				if (LadderDown != null) {
					if (calc.distanceTo(LadderDown) < 4) {
						camera.turnTo(LadderDown);
						LadderDown.doAction("Climb-down");
						SleepTime = 2000;
					} else {
						// walking.walkPathMM(toMine, 2 ,2);
						toMine.traverse();
						SleepTime = 1500;
					}
				} else {
					// walking.walkPathMM(toMine, 2 ,2);
					toMine.traverse();
				}
			} else {
				if (calc.distanceTo(MineTile) < 7) {
					if (!isObjectValid("Rocks")) {
						// log.severe("If you see this! THEN MAKE SURE TO REPORT THIS AT WORLDMINERPRO THREAD");
						while (isObjectValid("Rocks")) {
							if (calc.distanceTo(walking.getDestination()) < random(
									3, 5)) {
								toMine.traverse();
							}
						}
					}
				} else {
					// walking.walkPathMM(toMine, 2 ,2);
					while (calc.distanceTo(MineTile) > 6) {
						if (calc.distanceTo(walking.getDestination()) < random(
								3, 5)) {
							toMine.traverse();
						}
					}
				}
			}
			SleepTime = 1500;
		}
		sleep(SleepTime);
	}

	public void Bank() {
		SleepTime = 1;
		RSObject BankBooth = objects.getNearest(thebankid);
		if (calc.distanceTo(BankTile) < 5) {
			if (!isObjectValid("Banker")) {
				while (isObjectValid("Banker")) {
					if (calc.distanceTo(walking.getDestination()) < random(3, 5)) {
						toBank.traverse();
					}
				}
			}
			if (!bank.isOpen()) {
				BankBooth.doAction("Use-quickly");
				sleep(3000);
				SleepTime = 1500;
			}
			if (bank.isOpen()) {
				bank.depositAllExcept(pickID);
				bank.close();
				timebanked = timebanked + 1;
				SleepTime = 1500;
			}
		} else {
			SleepTime = 1500;
			if (MineGuild) {
				RSObject LadderUp = objects.getNearest(goladdup);
				if (LadderUp != null) {
					if (calc.distanceTo(LadderUp) < 4) {
						camera.turnTo(LadderUp);
						LadderUp.doAction("Climb-up");
						SleepTime = 1500;
					} else {
						// walking.walkPathMM(toBank, 2,2);
						toBank.traverse();
					}
				} else {
					// walking.walkPathMM(toBank, 2 ,2);
					toBank.traverse();
				}
			} else {
				// walking.walkPathMM(toBank, 2 ,2);

				if (calc.distanceTo(walking.getDestination()) < random(3, 5)) {
					toBank.traverse();
				}

			}
		}
		sleep(SleepTime);
	}

	public void AntiBan() {

	}

	@Override
	public void onRepaint(Graphics g) {
		if (Start) {
			if (paint) {

				drawModel(g, objects.getNearest(Rocks), Color.BLUE, "",
						Color.BLACK);

				int lvlGain = skills.getCurrentLevel(Skills.MINING) - startlvl;
				xpGained = skills.getCurrentExp(Skills.MINING) - startexp;
				if (xpGained == 0) {
				} else {
					long millis = System.currentTimeMillis() - startTime;
					int xpPerHour = (int) (3600000.0 / millis * xpGained);
					int orePerHour = (int) (3600000.0 / millis * OreMined);
					// int profPerHour = (int) (3600000.0 / millis * profit);
					long hours = millis / (1000 * 60 * 60);
					millis -= hours * (1000 * 60 * 60);
					long minutes = millis / (1000 * 60);
					millis -= minutes * (1000 * 60);
					long seconds = millis / 1000;
					int TTL = skills.getExpToNextLevel(Skills.MINING)
							/ xpPerHour;
					int XPtoLvl = skills.getExpToNextLevel(Skills.MINING);
					int T99 = (13034431 - skills.getCurrentExp(Skills.MINING))
							/ xpPerHour;
					int percent = skills.getPercentToNextLevel(Skills.MINING);
					countpaint = countpaint + 1;

					g.drawLine(0, mouse.getLocation().y, 765,
							mouse.getLocation().y);
					g.drawLine(mouse.getLocation().x, 0, mouse.getLocation().x,
							506);

					Rectangle r = new Rectangle(496, 345, 15, 14);
					if (r.contains(mouse.getLocation())) {
						HidePaint = true;
					} else {
						HidePaint = false;
					}
					// End Mouse
					if (HidePaint == true) {
						g.setColor(new Color(255, 0, 0, 250));
						g.fillRect(496, 345, 15, 14);
					}
					// Screen Paint
					if (HidePaint == false) {
						g.setColor(new Color(0, 0, 0, 203));
						g.fillRect(7, 345, 505, 128);
						g.setColor(new Color(255, 0, 0, 250));
						g.fillRect(496, 345, 15, 14);
						g.setColor(new Color(0, 0, 0));
						g.drawLine(510, 345, 496, 359);
						g.setColor(new Color(0, 0, 0));
						g.drawLine(510, 359, 496, 345);
						g.setFont(new Font("Comic Sans MS", 0, 16));
						g.setColor(new Color(255, 0, 0));
						g.drawString("Brian's World Miner Pro", 16, 360);
						g.setFont(new Font("Century Schoolbook", 0, 12));
						g.setColor(new Color(255, 0, 0));
						g.drawString("Runtime: " + hours + ":" + minutes + ":"
								+ seconds, 16, 375);
						g.setFont(new Font("Century Schoolbook", 0, 12));
						g.setColor(new Color(255, 0, 0));
						g.drawString("Exp/H: " + xpPerHour, 16, 389);
						g.setFont(new Font("Century Schoolbook", 0, 12));
						g.setColor(new Color(255, 0, 0));
						g.drawString("Ores Mined: " + OreMined, 16, 403);
						g.drawString("Gems Mined: " + GemsMined, 16, 417);
						g.drawString("TTL: " + TTL + " Hours", 16, 431);
						g.drawString("Exp TL: " + XPtoLvl, 16, 445);
						g.drawString("Time T99: " + T99 + " Hours", 16, 459);
						g.drawString(percent + "% to next level", 156, 375);
						g.drawString(
								"Exp: " + skills.getCurrentExp(Skills.MINING),
								156, 389);
						// g.drawString("Profit: " + profit + "gp", 156, 403);
						g.drawString("Levels Gained: " + lvlGain, 156, 417);
						g.drawString("Ores/H: " + orePerHour, 156, 431);
						g.drawString("Exp Gained: " + xpGained, 156, 445);
						g.drawString(
								"Level: "
										+ skills.getCurrentLevel(Skills.MINING),
								156, 459);
						// g.drawString("Profit/H: " + profPerHour, 296, 375);
						g.drawString("Banked: " + timebanked, 156, 403);
						// Cape Image

						// g.drawImage(cape, 441, 339, null);
					}

				}
			}
		}
		if (safepaint) {
			if (startexp == 0) {
				startexp = skills.getCurrentExp(Skills.MINING);
			}
			int lvlGain = skills.getCurrentLevel(Skills.MINING) - startlvl;
			xpGained = skills.getCurrentExp(Skills.MINING) - startexp;
			long millis = System.currentTimeMillis() - startTime;
			int orePerHour = (int) (3600000.0 / millis * OreMined);
			int xpPerHour = (int) (3600000.0 / millis * xpGained);
			long hours = millis / (1000 * 60 * 60);
			millis -= hours * (1000 * 60 * 60);
			long minutes = millis / (1000 * 60);
			millis -= minutes * (1000 * 60);
			long seconds = millis / 1000;

			g.setColor(new Color(0, 0, 0, 215));
			g.fillRect(7, 345, 507, 114);
			g.setFont(new Font("Calibri", 0, 20));
			g.setColor(new Color(255, 0, 0));
			g.drawString("WorldMinerPro", 13, 364);
			g.setFont(new Font("Calibri", 0, 14));
			g.setColor(new Color(255, 0, 0));
			g.drawString("Runtime: " + hours + ":" + minutes + ":" + seconds,
					14, 380);
			g.drawString("Gems Mined: " + GemsMined, 180, 380);
			g.setFont(new Font("Calibri", 0, 14));
			g.setColor(new Color(255, 0, 0));
			g.drawString("Exp Gained: " + xpGained, 13, 394);

			g.setFont(new Font("Calibri", 0, 14));
			g.setColor(new Color(255, 0, 0));
			g.drawString("Exp/H: " + xpPerHour, 14, 409);
			g.setFont(new Font("Calibri", 0, 14));
			g.setColor(new Color(255, 0, 0));
			g.drawString("Ores Mined: " + OreMined, 14, 424);
			g.drawString("Level: " + skills.getCurrentLevel(Skills.MINING),
					180, 424);
			g.setFont(new Font("Calibri", 0, 14));
			g.setColor(new Color(255, 0, 0));
			g.drawString("Ores/H: " + orePerHour, 14, 438);
			g.drawString("EXP: " + skills.getCurrentExp(Skills.MINING), 180,
					438);
			g.setFont(new Font("Calibri", 0, 14));
			g.setColor(new Color(255, 0, 0));
			g.drawString("Levels Gained: " + lvlGain, 14, 452);
			g.setFont(new Font("Calibri", 0, 14));
			g.setColor(new Color(255, 0, 0));
			// g.drawString("Profit:" + profit + " gp", 180, 452);
			g.setFont(new Font("Calibri", 0, 14));
			g.setColor(new Color(255, 0, 0));
			g.drawString("Brianpsv1", 451, 452);

			g.setColor(Color.BLACK);
			g.setColor(Color.RED);

			final int percent = skills.getPercentToNextLevel(Skills.MINING);

			g.setColor(Color.red);
			g.fillRoundRect(180, 400, 100, 10, 15, 15); // these must be on same
														// cordinates
			g.setColor(Color.black);
			g.fillRoundRect(180, 400, percent, 10, 15, 15); // these must be on
															// same cordinates
			g.setColor(Color.red);
			g.drawString("" + percent + "%", 220, 410); // this must be in the
														// center of the bar
			g.setColor(Color.blue);

			g.setColor(Color.black);
			g.drawRoundRect(180, 400, 100, 10, 15, 15); // these must be on same
														// cordinates
		}

	}

	@Override
	public void onFinish() {
		log("Thanks for using.");
	}

	public boolean onHorizontalOrVertTile(RSTile tile) {
		boolean bool = false;
		if (getMyPlayer().getLocation().getX() == tile.getX()
				|| getMyPlayer().getLocation().getY() == tile.getY()) {
			if (calc.distanceTo(tile) == 1) {
				bool = true;
			} else
				bool = false;
		}
		return bool;
	}

	public void drawModel(Graphics g, RSObject o, Color c, String s, Color tc) {
		if (o != null) {
			Polygon[] model = o.getModel().getTriangles();
			Point point = calc.tileToScreen(o.getLocation());
			for (Polygon p : model) {
				g.setColor(c);
				g.fillPolygon(p);
				g.setColor(c.darker());
				g.drawPolygon(p);
			}

			g.setColor(tc);
			g.drawString(s, point.x - 75, point.y - 35);
		}
	}

	public RSTile[] reversePath(RSTile[] other) {
		RSTile[] t = new RSTile[other.length];
		for (int i = 0; i < t.length; i++) {
			t[i] = other[other.length - i - 1];
		}
		return t;
	}

	/*
	 * public boolean isObjectValid(final String ObjName) { objects.getAll(new
	 * Filter<RSObject>() { public boolean accept(RSObject t) { boolean b =
	 * false; try { String a = t.getDef().getName(); if(a == ObjName) { b =
	 * true; }
	 * 
	 * } catch(NullPointerException ignored) {}
	 * 
	 * return b; } }); return true; }
	 */
	public boolean isObjectValid(final String name) {
		return objects.getAll(new Filter<RSObject>() {
			@Override
			public boolean accept(RSObject t) {

				RSObjectDef def = t.getDef();

				return def != null && def.getName() != null
						&& def.getName().equals(name);
			}
		}).length > 0;
	}

	public RSObject getSecondNearest(final RSObject curRock, final int... ids) {
		return objects.getNearest(new Filter<RSObject>() {
			@Override
			public boolean accept(RSObject o) {
				if (curRock.equals(o))
					return false;
				for (int id : ids)
					if (o.getID() == id)
						return true;
				return false;
			}
		});
	}

	@SuppressWarnings("serial")
	public class minegui extends javax.swing.JFrame {

		/** Creates new form minegui */
		public minegui() {
			initComponents();
		}

		/**
		 * This method is called from within the constructor to initialize the
		 * form. WARNING: Do NOT modify this code. The content of this method is
		 * always regenerated by the Form Editor.
		 */
		private void initComponents() {

			jScrollPane1 = new javax.swing.JScrollPane();
			jTree1 = new javax.swing.JTree();
			jLabel1 = new javax.swing.JLabel();
			jButton1 = new javax.swing.JButton();
			jLabel2 = new javax.swing.JLabel();
			jSlider1 = new javax.swing.JSlider();
			jLabel3 = new javax.swing.JLabel();
			jComboBox1 = new javax.swing.JComboBox();
			jLabel4 = new javax.swing.JLabel();
			jCheckBox1 = new javax.swing.JCheckBox();

			setTitle("Brian's WorldMinerPro");
			setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
			setResizable(false);

			jTree1.setFont(new java.awt.Font("Comic Sans MS", 0, 11)); // NOI18N
			javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode(
					"Miner");
			javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode(
					"Banking");
			javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"East Varrock [Iron]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"South Varrock [Iron]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Iron]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Rimmington [Iron]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Rimmington [Copper]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Lumbridge Swamp [Coal]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Barbarian Village [Coal]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Barbarian Village [Tin]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Mining Guild [Coal]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Mining Guild [Coal, Mithril]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Mithril]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Silver]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Adamant]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Copper]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Coal]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Tin]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Gold]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Yanille [Iron]");
			treeNode2.add(treeNode3);
			treeNode1.add(treeNode2);
			treeNode2 = new javax.swing.tree.DefaultMutableTreeNode(
					"Powermining");
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"East Varrock [Iron]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"South Varrock [Iron]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Iron]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Mithril]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Silver]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Adamant]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Copper]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Coal]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Tin]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Al Kharid [Gold]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Rimmington [Iron]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Lumbridge Swamp [Coal]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Barbarian Village [Coal]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Barbarian Village [Tin]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Mining Guild [Coal]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Mining Guild [Coal, Mithril]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Crafting Guild [Gold]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Crafting Guild [Silver]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Crafting Guild [Clay]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Rimmington [Clay]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Rimmington [Copper]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Karamja Dungeon [Gold]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Crandor [Coal]");
			treeNode2.add(treeNode3);
			treeNode3 = new javax.swing.tree.DefaultMutableTreeNode(
					"Lumbridge Swamp [Copper]");
			treeNode2.add(treeNode3);
			treeNode1.add(treeNode2);
			jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
			jTree1.setCursor(new java.awt.Cursor(
					java.awt.Cursor.CROSSHAIR_CURSOR));
			jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					jTree1MouseClicked(evt);
				}
			});
			jScrollPane1.setViewportView(jTree1);

			jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 12)); // NOI18N
			jLabel1.setText("Location");

			jButton1.setFont(new java.awt.Font("Comic Sans MS", 0, 11)); // NOI18N
			jButton1.setText("Run");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					jButton1ActionPerformed(evt);
				}
			});

			jSlider1.setMaximum(7);
			jSlider1.setMinimum(1);
			jSlider1.setPaintTicks(true);

			jLabel3.setFont(new java.awt.Font("Comic Sans MS", 0, 11)); // NOI18N
			jLabel3.setText("Mouse Speed");

			jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(
					new String[] { "Red & Black", "Safe Paint", "None" }));

			jLabel4.setFont(new java.awt.Font("Comic Sans MS", 0, 11)); // NOI18N
			jLabel4.setText("Paint");

			jCheckBox1.setText("Rest");

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
					getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGroup(
							layout.createSequentialGroup()
									.addComponent(
											jScrollPane1,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											226,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.LEADING)
													.addGroup(
															layout.createSequentialGroup()
																	.addGroup(
																			layout.createParallelGroup(
																					javax.swing.GroupLayout.Alignment.TRAILING)
																					.addGroup(
																							layout.createSequentialGroup()
																									.addPreferredGap(
																											javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																									.addGroup(
																											layout.createParallelGroup(
																													javax.swing.GroupLayout.Alignment.TRAILING)
																													.addComponent(
																															jLabel1,
																															javax.swing.GroupLayout.Alignment.LEADING)
																													.addComponent(
																															jLabel2,
																															javax.swing.GroupLayout.Alignment.LEADING,
																															javax.swing.GroupLayout.DEFAULT_SIZE,
																															459,
																															Short.MAX_VALUE)))
																					.addGroup(
																							layout.createSequentialGroup()
																									.addGroup(
																											layout.createParallelGroup(
																													javax.swing.GroupLayout.Alignment.LEADING)
																													.addGroup(
																															layout.createSequentialGroup()
																																	.addGap(6,
																																			6,
																																			6)
																																	.addComponent(
																																			jComboBox1,
																																			javax.swing.GroupLayout.PREFERRED_SIZE,
																																			144,
																																			javax.swing.GroupLayout.PREFERRED_SIZE)
																																	.addPreferredGap(
																																			javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																																			129,
																																			Short.MAX_VALUE)
																																	.addComponent(
																																			jCheckBox1))
																													.addGroup(
																															layout.createSequentialGroup()
																																	.addGap(57,
																																			57,
																																			57)
																																	.addComponent(
																																			jLabel4)))
																									.addGap(18,
																											18,
																											18)
																									.addComponent(
																											jButton1,
																											javax.swing.GroupLayout.PREFERRED_SIZE,
																											107,
																											javax.swing.GroupLayout.PREFERRED_SIZE)
																									.addGap(12,
																											12,
																											12))
																					.addGroup(
																							javax.swing.GroupLayout.Alignment.LEADING,
																							layout.createSequentialGroup()
																									.addPreferredGap(
																											javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																									.addComponent(
																											jSlider1,
																											javax.swing.GroupLayout.DEFAULT_SIZE,
																											453,
																											Short.MAX_VALUE)))
																	.addGap(0,
																			0,
																			0))
													.addGroup(
															javax.swing.GroupLayout.Alignment.TRAILING,
															layout.createSequentialGroup()
																	.addPreferredGap(
																			javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																	.addComponent(
																			jLabel3)
																	.addGap(192,
																			192,
																			192)))));
			layout.setVerticalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addComponent(jScrollPane1,
							javax.swing.GroupLayout.DEFAULT_SIZE, 354,
							Short.MAX_VALUE)
					.addGroup(
							layout.createSequentialGroup()
									.addContainerGap()
									.addComponent(jLabel1)
									.addPreferredGap(
											javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(
											jLabel2,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											186,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addGap(16, 16, 16)
									.addComponent(jLabel3)
									.addPreferredGap(
											javax.swing.LayoutStyle.ComponentPlacement.RELATED)
									.addComponent(
											jSlider1,
											javax.swing.GroupLayout.PREFERRED_SIZE,
											javax.swing.GroupLayout.DEFAULT_SIZE,
											javax.swing.GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(
											javax.swing.LayoutStyle.ComponentPlacement.RELATED,
											27, Short.MAX_VALUE)
									.addGroup(
											layout.createParallelGroup(
													javax.swing.GroupLayout.Alignment.BASELINE)
													.addComponent(jCheckBox1)
													.addComponent(jButton1)
													.addComponent(
															jComboBox1,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.PREFERRED_SIZE))
									.addContainerGap())
					.addGroup(
							layout.createSequentialGroup()
									.addGap(302, 302, 302)
									.addComponent(jLabel4)
									.addContainerGap(35, Short.MAX_VALUE)));

			pack();
		}// </editor-fold>

		private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {
			try {
				jLabel1.setText(jTree1.getLastSelectedPathComponent()
						.toString());
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Lumbridge Swamp [Copper]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i56.tinypic.com/2ilejbp.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Lumbridge Swamp [Coal]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i53.tinypic.com/2v8fkft.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("East Varrock [Iron]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i54.tinypic.com/ak7803.jpg"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("South Varrock [Iron]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i51.tinypic.com/o7uk5e.jpg"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Crafting Guild [Gold]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i55.tinypic.com/6pt5w1.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Crafting Guild [Silver]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i56.tinypic.com/oacdj5.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Crafting Guild [Clay]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i51.tinypic.com/sw4nl4.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Rimmington [Iron]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i56.tinypic.com/hvqovm.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Rimmington [Clay]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i54.tinypic.com/jkgd5k.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Rimmington [Copper]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i51.tinypic.com/qya36b.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Mining Guild [Coal]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i56.tinypic.com/xf0wlw.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Mining Guild [Coal, Mithril]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i56.tinypic.com/xf0wlw.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}

				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Al Kharid [Iron]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i53.tinypic.com/97l5bm.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
				if (jTree1.getLastSelectedPathComponent().toString()
						.contains("Barbarian Village [Coal]")) {
					jLabel2.setIcon(new javax.swing.JLabel() {
						@Override
						public javax.swing.Icon getIcon() {

							try {
								return new javax.swing.ImageIcon(
										new java.net.URL(
												"http://i51.tinypic.com/2guxxmf.png"));
							} catch (java.net.MalformedURLException e) {
							}
							return null;
						}
					}.getIcon());
				}
			} catch (NullPointerException e) {

			}
		}

		private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
			MouseSpeed = jSlider1.getValue();
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Barbarian Village [Coal]")) {
				RSTile[] path = { new RSTile(3084, 3421),
						new RSTile(3085, 3433), new RSTile(3085, 3439),
						new RSTile(3090, 3449), new RSTile(3089, 3455),
						new RSTile(3087, 3461), new RSTile(3082, 3466),
						new RSTile(3080, 3476), new RSTile(3080, 3484),
						new RSTile(3086, 3488), new RSTile(3094, 3491) };
				RSTile bank = new RSTile(3094, 3491);
				RSTile mine = new RSTile(3084, 3420);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 11932, 11930, 11931, 5770, 5771, 5772 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Barbarian Village [Tin]")) {
				RSTile[] path = { new RSTile(3084, 3421),
						new RSTile(3085, 3433), new RSTile(3085, 3439),
						new RSTile(3090, 3449), new RSTile(3089, 3455),
						new RSTile(3087, 3461), new RSTile(3082, 3466),
						new RSTile(3080, 3476), new RSTile(3080, 3484),
						new RSTile(3086, 3488), new RSTile(3094, 3491) };
				RSTile bank = new RSTile(3094, 3491);
				RSTile mine = new RSTile(3084, 3420);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 11934, 11933, 11935 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Lumbridge Swamp [Coal]")) {
				RSTile[] path = { new RSTile(3146, 3150),
						new RSTile(3145, 3156), new RSTile(3146, 3158),
						new RSTile(3145, 3160), new RSTile(3145, 3161),
						new RSTile(3144, 3163), new RSTile(3145, 3164),
						new RSTile(3144, 3167), new RSTile(3144, 3168),
						new RSTile(3143, 3169), new RSTile(3144, 3171),
						new RSTile(3143, 3173), new RSTile(3143, 3174),
						new RSTile(3143, 3175), new RSTile(3142, 3177),
						new RSTile(3142, 3179), new RSTile(3142, 3180),
						new RSTile(3142, 3181), new RSTile(3142, 3182),
						new RSTile(3143, 3184), new RSTile(3142, 3186),
						new RSTile(3142, 3187), new RSTile(3142, 3189),
						new RSTile(3142, 3191), new RSTile(3141, 3194),
						new RSTile(3141, 3195), new RSTile(3140, 3196),
						new RSTile(3140, 3199), new RSTile(3140, 3201),
						new RSTile(3139, 3202), new RSTile(3139, 3203),
						new RSTile(3139, 3204), new RSTile(3138, 3206),
						new RSTile(3136, 3205), new RSTile(3135, 3206),
						new RSTile(3131, 3206), new RSTile(3128, 3207),
						new RSTile(3122, 3210), new RSTile(3125, 3208),
						new RSTile(3123, 3209), new RSTile(3122, 3209),
						new RSTile(3122, 3211), new RSTile(3120, 3213),
						new RSTile(3120, 3214), new RSTile(3119, 3214),
						new RSTile(3118, 3216), new RSTile(3117, 3217),
						new RSTile(3117, 3219), new RSTile(3115, 3220),
						new RSTile(3114, 3222), new RSTile(3113, 3222),
						new RSTile(3112, 3224), new RSTile(3111, 3224),
						new RSTile(3110, 3225), new RSTile(3111, 3227),
						new RSTile(3110, 3227), new RSTile(3110, 3229),
						new RSTile(3110, 3230), new RSTile(3110, 3232),
						new RSTile(3109, 3233), new RSTile(3106, 3237),
						new RSTile(3105, 3238), new RSTile(3104, 3240),
						new RSTile(3103, 3241), new RSTile(3103, 3243),
						new RSTile(3101, 3244), new RSTile(3100, 3245),
						new RSTile(3099, 3245), new RSTile(3097, 3247),
						new RSTile(3094, 3247), new RSTile(3092, 3248),
						new RSTile(3092, 3244) };
				RSTile bank = new RSTile(3092, 3244);
				RSTile mine = new RSTile(3146, 3150);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 3032, 3233 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Al Kharid [Iron]")) {
				RSTile[] path = { new RSTile(3303, 3312),
						new RSTile(3301, 3312), new RSTile(3299, 3311),
						new RSTile(3298, 3307), new RSTile(3299, 3306),
						new RSTile(3298, 3301), new RSTile(3298, 3299),
						new RSTile(3299, 3295), new RSTile(3300, 3291),
						new RSTile(3299, 3288), new RSTile(3298, 3284),
						new RSTile(3297, 3282), new RSTile(3295, 3280),
						new RSTile(3296, 3276), new RSTile(3296, 3275),
						new RSTile(3296, 3274), new RSTile(3295, 3272),
						new RSTile(3294, 3270), new RSTile(3294, 3269),
						new RSTile(3294, 3267), new RSTile(3292, 3265),
						new RSTile(3293, 3262), new RSTile(3294, 3260),
						new RSTile(3293, 3258), new RSTile(3294, 3255),
						new RSTile(3294, 3253), new RSTile(3293, 3250),
						new RSTile(3286, 3238), new RSTile(3279, 3226),
						new RSTile(3278, 3213), new RSTile(3275, 3200),
						new RSTile(3281, 3191), new RSTile(3300, 3198),
						new RSTile(3298, 3198), new RSTile(3295, 3198),
						new RSTile(3280, 3196), new RSTile(3281, 3192),
						new RSTile(3281, 3189), new RSTile(3283, 3185),
						new RSTile(3281, 3184), new RSTile(3279, 3181),
						new RSTile(3276, 3180), new RSTile(3278, 3177),
						new RSTile(3276, 3173), new RSTile(3275, 3168),
						new RSTile(3274, 3167), new RSTile(3270, 3168),
						new RSTile(3269, 3168) };
				RSTile bank = new RSTile(3269, 3167);
				RSTile mine = new RSTile(3301, 3310);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 37309, 37307, 37308 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("South Varrock [Iron]")) {
				RSTile[] path = { new RSTile(3287, 3370),
						new RSTile(3291, 3375), new RSTile(3293, 3374),
						new RSTile(3292, 3376), new RSTile(3294, 3382),
						new RSTile(3291, 3394), new RSTile(3291, 3406),
						new RSTile(3287, 3419), new RSTile(3278, 3429),
						new RSTile(3268, 3429), new RSTile(3258, 3429),
						new RSTile(3253, 3420) };
				RSTile bank = new RSTile(3253, 3420);
				RSTile mine = new RSTile(3287, 3370);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 9719, 9718, 9717, 11954, 11955, 11956,
						37307, 37308, 37309 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("East Varrock [Iron]")) {
				RSTile[] path = { new RSTile(3175, 3367),
						new RSTile(3172, 3372), new RSTile(3173, 3381),
						new RSTile(3172, 3387), new RSTile(3172, 3394),
						new RSTile(3170, 3403), new RSTile(3170, 3413),
						new RSTile(3170, 3421), new RSTile(3176, 3428),
						new RSTile(3183, 3430), new RSTile(3187, 3436) };
				RSTile bank = new RSTile(3187, 3436);
				RSTile mine = new RSTile(3175, 3367);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 9719, 9718, 9717, 11954, 11955, 11956,
						37307, 37308, 37309 };
				Rocks = mineRock;
			}
			if (jTree1.getSelectionPath().toString().contains("Powermining")) {
				PowerMine = true;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Rimmington [Iron]")) {
				RSTile[] path = { new RSTile(2968, 3240),
						new RSTile(2970, 3244), new RSTile(2974, 3248),
						new RSTile(2977, 3254), new RSTile(2981, 3260),
						new RSTile(2985, 3266), new RSTile(2990, 3273),
						new RSTile(2999, 3278), new RSTile(3004, 3285),
						new RSTile(3006, 3293), new RSTile(3005, 3300),
						new RSTile(3005, 3308), new RSTile(3006, 3314),
						new RSTile(3006, 3322), new RSTile(3006, 3330),
						new RSTile(3006, 3335), new RSTile(3007, 3340),
						new RSTile(3007, 3344), new RSTile(3007, 3348),
						new RSTile(3006, 3351), new RSTile(3005, 3354),
						new RSTile(3006, 3357), new RSTile(3007, 3360),
						new RSTile(3010, 3360), new RSTile(3012, 3359),
						new RSTile(3012, 3355) };
				RSTile bank = new RSTile(3012, 3355);
				RSTile mine = new RSTile(2968, 3240);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 9719, 9718, 9717, 11954, 11955, 11956,
						37307, 37308, 37309 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Rimmington [Copper]")) {
				RSTile[] path = { new RSTile(2968, 3240),
						new RSTile(2970, 3244), new RSTile(2974, 3248),
						new RSTile(2977, 3254), new RSTile(2981, 3260),
						new RSTile(2985, 3266), new RSTile(2990, 3273),
						new RSTile(2999, 3278), new RSTile(3004, 3285),
						new RSTile(3006, 3293), new RSTile(3005, 3300),
						new RSTile(3005, 3308), new RSTile(3006, 3314),
						new RSTile(3006, 3322), new RSTile(3006, 3330),
						new RSTile(3006, 3335), new RSTile(3007, 3340),
						new RSTile(3007, 3344), new RSTile(3007, 3348),
						new RSTile(3006, 3351), new RSTile(3005, 3354),
						new RSTile(3006, 3357), new RSTile(3007, 3360),
						new RSTile(3010, 3360), new RSTile(3012, 3359),
						new RSTile(3012, 3355) };
				RSTile bank = new RSTile(3012, 3355);
				RSTile mine = new RSTile(2977, 3248);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 9708, 9709, 9710, 11937, 19936, 11938 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Rimmington [Clay]")) {
				int[] mineRock = { 11189, 11190, 11191, 9711, 9713 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Crafting Guild [Clay]")) {
				int[] mineRock = { 11189, 11190, 11191, 9711, 9713 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Crafting Guild [Gold]")) {
				int[] mineRock = { 11183, 11184, 11185 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Crandor [Coal]")) {
				int[] mineRock = { 14850, 14852, 14851 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Crafting Guild [Silver]")) {
				int[] mineRock = { 11186, 11187, 11188 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Lumbridge Swamp [Copper]")) {
				int[] mineRock = { 3229, 3027 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Karamja Dungeon [Gold]")) {
				int[] mineRock = { 32432, 32434, 32433 };
				Rocks = mineRock;
			}
			if (jTree1.getSelectionPath().toString().contains("Powermining")) {
				PowerMine = true;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Mining Guild [Coal]")) {
				RSTile[] path = { new RSTile(3045, 9748),
						new RSTile(3045, 9741), new RSTile(3041, 9738),
						new RSTile(3035, 9738), new RSTile(3029, 9738),
						new RSTile(3024, 9739), new RSTile(3021, 9739),
						new RSTile(3021, 3339), new RSTile(3028, 3336),
						new RSTile(3029, 3342), new RSTile(3028, 3346),
						new RSTile(3025, 3349), new RSTile(3022, 3353),
						new RSTile(3019, 3358), new RSTile(3015, 3360),
						new RSTile(3013, 3355) };
				RSTile bank = new RSTile(3013, 3355);
				RSTile mine = new RSTile(3045, 9748);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 11932, 11930, 11931, 5770, 5771, 5772 };
				Rocks = mineRock;
				MineGuild = true;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Mining Guild [Coal, Mithril]")) {
				RSTile[] path = { new RSTile(3045, 9748),
						new RSTile(3045, 9741), new RSTile(3041, 9738),
						new RSTile(3035, 9738), new RSTile(3029, 9738),
						new RSTile(3024, 9739), new RSTile(3021, 9739),
						new RSTile(3021, 3339), new RSTile(3028, 3336),
						new RSTile(3029, 3342), new RSTile(3028, 3346),
						new RSTile(3025, 3349), new RSTile(3022, 3353),
						new RSTile(3019, 3358), new RSTile(3015, 3360),
						new RSTile(3013, 3355) };
				RSTile bank = new RSTile(3013, 3355);
				RSTile mine = new RSTile(3045, 9748);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 11932, 11930, 11931, 5770, 5771, 5772, 5786,
						5785, 5784 };
				Rocks = mineRock;
				MineGuild = true;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Yanille [Iron]")) {
				RSTile[] path = { new RSTile(2629, 3136),
						new RSTile(2627, 3127), new RSTile(2623, 3115),
						new RSTile(2615, 3104), new RSTile(2612, 3092) };
				RSTile bank = new RSTile(2612, 3092);
				RSTile mine = new RSTile(2629, 3136);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 37307, 37308, 37309 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Wilderness Goblins [Coal]")) {
				RSTile[] path = { new RSTile(3096, 3494),
						new RSTile(3088, 3493), new RSTile(3088, 3495),
						new RSTile(3088, 3496), new RSTile(3087, 3499),
						new RSTile(3087, 3500), new RSTile(3088, 3502),
						new RSTile(3088, 3503), new RSTile(3088, 3504),
						new RSTile(3088, 3505), new RSTile(3088, 3507),
						new RSTile(3087, 3508), new RSTile(3088, 3509),
						new RSTile(3087, 3510), new RSTile(3087, 3512),
						new RSTile(3088, 3513), new RSTile(3088, 3515),
						new RSTile(3087, 3517), new RSTile(3087, 3519),
						new RSTile(3087, 3521), new RSTile(3087, 3524),
						new RSTile(3086, 3526), new RSTile(3085, 3528),
						new RSTile(3084, 3530), new RSTile(3083, 3532),
						new RSTile(3082, 3534), new RSTile(3081, 3536),
						new RSTile(3080, 3538), new RSTile(3078, 3541),
						new RSTile(3074, 3545), new RSTile(3071, 3548),
						new RSTile(3071, 3550), new RSTile(3069, 3551),
						new RSTile(3068, 3553), new RSTile(3066, 3556),
						new RSTile(3064, 3559), new RSTile(3062, 3560),
						new RSTile(3060, 3562), new RSTile(3058, 3564),
						new RSTile(3056, 3566), new RSTile(3053, 3568),
						new RSTile(3051, 3570), new RSTile(3049, 3572),
						new RSTile(3046, 3574), new RSTile(3044, 3576),
						new RSTile(3041, 3577), new RSTile(3040, 3579),
						new RSTile(3038, 3580), new RSTile(3037, 3581),
						new RSTile(3035, 3583), new RSTile(3032, 3585),
						new RSTile(3030, 3586), new RSTile(3027, 3587),
						new RSTile(3025, 3588), new RSTile(3024, 3590),
						new RSTile(3022, 3592), new RSTile(3020, 3593),
						new RSTile(3019, 3593) };
				RSTile bank = new RSTile(3096, 3494);
				RSTile mine = new RSTile(3019, 3593);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = walking.reversePath(path);
				// toMine = path;
				int[] mineRock = { 11932, 11930, 11931, 5770, 5771, 5772 };
				Rocks = mineRock;
				wildi = true;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Al Kharid [Copper]")) {
				RSTile[] path = { new RSTile(3303, 3312),
						new RSTile(3301, 3312), new RSTile(3299, 3311),
						new RSTile(3298, 3307), new RSTile(3299, 3306),
						new RSTile(3298, 3301), new RSTile(3298, 3299),
						new RSTile(3299, 3295), new RSTile(3300, 3291),
						new RSTile(3299, 3288), new RSTile(3298, 3284),
						new RSTile(3297, 3282), new RSTile(3295, 3280),
						new RSTile(3296, 3276), new RSTile(3296, 3275),
						new RSTile(3296, 3274), new RSTile(3295, 3272),
						new RSTile(3294, 3270), new RSTile(3294, 3269),
						new RSTile(3294, 3267), new RSTile(3292, 3265),
						new RSTile(3293, 3262), new RSTile(3294, 3260),
						new RSTile(3293, 3258), new RSTile(3294, 3255),
						new RSTile(3294, 3253), new RSTile(3293, 3250),
						new RSTile(3286, 3238), new RSTile(3279, 3226),
						new RSTile(3278, 3213), new RSTile(3275, 3200),
						new RSTile(3281, 3191), new RSTile(3300, 3198),
						new RSTile(3298, 3198), new RSTile(3295, 3198),
						new RSTile(3280, 3196), new RSTile(3281, 3192),
						new RSTile(3281, 3189), new RSTile(3283, 3185),
						new RSTile(3281, 3184), new RSTile(3279, 3181),
						new RSTile(3276, 3180), new RSTile(3278, 3177),
						new RSTile(3276, 3173), new RSTile(3275, 3168),
						new RSTile(3274, 3167), new RSTile(3270, 3168),
						new RSTile(3269, 3168) };
				RSTile bank = new RSTile(3269, 3167);
				RSTile mine = new RSTile(3301, 3310);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 11936, 11937, 11938 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Al Kharid [Mithril]")) {
				RSTile[] path = { new RSTile(3303, 3312),
						new RSTile(3301, 3312), new RSTile(3299, 3311),
						new RSTile(3298, 3307), new RSTile(3299, 3306),
						new RSTile(3298, 3301), new RSTile(3298, 3299),
						new RSTile(3299, 3295), new RSTile(3300, 3291),
						new RSTile(3299, 3288), new RSTile(3298, 3284),
						new RSTile(3297, 3282), new RSTile(3295, 3280),
						new RSTile(3296, 3276), new RSTile(3296, 3275),
						new RSTile(3296, 3274), new RSTile(3295, 3272),
						new RSTile(3294, 3270), new RSTile(3294, 3269),
						new RSTile(3294, 3267), new RSTile(3292, 3265),
						new RSTile(3293, 3262), new RSTile(3294, 3260),
						new RSTile(3293, 3258), new RSTile(3294, 3255),
						new RSTile(3294, 3253), new RSTile(3293, 3250),
						new RSTile(3286, 3238), new RSTile(3279, 3226),
						new RSTile(3278, 3213), new RSTile(3275, 3200),
						new RSTile(3281, 3191), new RSTile(3300, 3198),
						new RSTile(3298, 3198), new RSTile(3295, 3198),
						new RSTile(3280, 3196), new RSTile(3281, 3192),
						new RSTile(3281, 3189), new RSTile(3283, 3185),
						new RSTile(3281, 3184), new RSTile(3279, 3181),
						new RSTile(3276, 3180), new RSTile(3278, 3177),
						new RSTile(3276, 3173), new RSTile(3275, 3168),
						new RSTile(3274, 3167), new RSTile(3270, 3168),
						new RSTile(3269, 3168) };
				RSTile bank = new RSTile(3269, 3167);
				RSTile mine = new RSTile(3301, 3310);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 11942, 11944 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Al Kharid [Silver]")) {
				RSTile[] path = { new RSTile(3303, 3312),
						new RSTile(3301, 3312), new RSTile(3299, 3311),
						new RSTile(3298, 3307), new RSTile(3299, 3306),
						new RSTile(3298, 3301), new RSTile(3298, 3299),
						new RSTile(3299, 3295), new RSTile(3300, 3291),
						new RSTile(3299, 3288), new RSTile(3298, 3284),
						new RSTile(3297, 3282), new RSTile(3295, 3280),
						new RSTile(3296, 3276), new RSTile(3296, 3275),
						new RSTile(3296, 3274), new RSTile(3295, 3272),
						new RSTile(3294, 3270), new RSTile(3294, 3269),
						new RSTile(3294, 3267), new RSTile(3292, 3265),
						new RSTile(3293, 3262), new RSTile(3294, 3260),
						new RSTile(3293, 3258), new RSTile(3294, 3255),
						new RSTile(3294, 3253), new RSTile(3293, 3250),
						new RSTile(3286, 3238), new RSTile(3279, 3226),
						new RSTile(3278, 3213), new RSTile(3275, 3200),
						new RSTile(3281, 3191), new RSTile(3300, 3198),
						new RSTile(3298, 3198), new RSTile(3295, 3198),
						new RSTile(3280, 3196), new RSTile(3281, 3192),
						new RSTile(3281, 3189), new RSTile(3283, 3185),
						new RSTile(3281, 3184), new RSTile(3279, 3181),
						new RSTile(3276, 3180), new RSTile(3278, 3177),
						new RSTile(3276, 3173), new RSTile(3275, 3168),
						new RSTile(3274, 3167), new RSTile(3270, 3168),
						new RSTile(3269, 3168) };
				RSTile bank = new RSTile(3269, 3167);
				RSTile mine = new RSTile(3301, 3310);
				BankTile = bank;
				MineTile = mine;
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 37304, 37306, 37305, 2311 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Al Kharid [Adamant]")) {
				RSTile[] path = { new RSTile(3303, 3312),
						new RSTile(3301, 3312), new RSTile(3299, 3311),
						new RSTile(3298, 3307), new RSTile(3299, 3306),
						new RSTile(3298, 3301), new RSTile(3298, 3299),
						new RSTile(3299, 3295), new RSTile(3300, 3291),
						new RSTile(3299, 3288), new RSTile(3298, 3284),
						new RSTile(3297, 3282), new RSTile(3295, 3280),
						new RSTile(3296, 3276), new RSTile(3296, 3275),
						new RSTile(3296, 3274), new RSTile(3295, 3272),
						new RSTile(3294, 3270), new RSTile(3294, 3269),
						new RSTile(3294, 3267), new RSTile(3292, 3265),
						new RSTile(3293, 3262), new RSTile(3294, 3260),
						new RSTile(3293, 3258), new RSTile(3294, 3255),
						new RSTile(3294, 3253), new RSTile(3293, 3250),
						new RSTile(3286, 3238), new RSTile(3279, 3226),
						new RSTile(3278, 3213), new RSTile(3275, 3200),
						new RSTile(3281, 3191), new RSTile(3300, 3198),
						new RSTile(3298, 3198), new RSTile(3295, 3198),
						new RSTile(3280, 3196), new RSTile(3281, 3192),
						new RSTile(3281, 3189), new RSTile(3283, 3185),
						new RSTile(3281, 3184), new RSTile(3279, 3181),
						new RSTile(3276, 3180), new RSTile(3278, 3177),
						new RSTile(3276, 3173), new RSTile(3275, 3168),
						new RSTile(3274, 3167), new RSTile(3270, 3168),
						new RSTile(3269, 3168) };
				RSTile bank = new RSTile(3269, 3167);
				RSTile mine = new RSTile(3301, 3310);
				BankTile = bank;
				MineTile = mine;
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 11939, 11941 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Al Kharid [Coal]")) {
				RSTile[] path = { new RSTile(3303, 3312),
						new RSTile(3301, 3312), new RSTile(3299, 3311),
						new RSTile(3298, 3307), new RSTile(3299, 3306),
						new RSTile(3298, 3301), new RSTile(3298, 3299),
						new RSTile(3299, 3295), new RSTile(3300, 3291),
						new RSTile(3299, 3288), new RSTile(3298, 3284),
						new RSTile(3297, 3282), new RSTile(3295, 3280),
						new RSTile(3296, 3276), new RSTile(3296, 3275),
						new RSTile(3296, 3274), new RSTile(3295, 3272),
						new RSTile(3294, 3270), new RSTile(3294, 3269),
						new RSTile(3294, 3267), new RSTile(3292, 3265),
						new RSTile(3293, 3262), new RSTile(3294, 3260),
						new RSTile(3293, 3258), new RSTile(3294, 3255),
						new RSTile(3294, 3253), new RSTile(3293, 3250),
						new RSTile(3286, 3238), new RSTile(3279, 3226),
						new RSTile(3278, 3213), new RSTile(3275, 3200),
						new RSTile(3281, 3191), new RSTile(3300, 3198),
						new RSTile(3298, 3198), new RSTile(3295, 3198),
						new RSTile(3280, 3196), new RSTile(3281, 3192),
						new RSTile(3281, 3189), new RSTile(3283, 3185),
						new RSTile(3281, 3184), new RSTile(3279, 3181),
						new RSTile(3276, 3180), new RSTile(3278, 3177),
						new RSTile(3276, 3173), new RSTile(3275, 3168),
						new RSTile(3274, 3167), new RSTile(3270, 3168),
						new RSTile(3269, 3168) };
				RSTile bank = new RSTile(3269, 3167);
				RSTile mine = new RSTile(3301, 3310);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 11932, 11930 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Al Kharid [Tin]")) {
				RSTile[] path = { new RSTile(3303, 3312),
						new RSTile(3301, 3312), new RSTile(3299, 3311),
						new RSTile(3298, 3307), new RSTile(3299, 3306),
						new RSTile(3298, 3301), new RSTile(3298, 3299),
						new RSTile(3299, 3295), new RSTile(3300, 3291),
						new RSTile(3299, 3288), new RSTile(3298, 3284),
						new RSTile(3297, 3282), new RSTile(3295, 3280),
						new RSTile(3296, 3276), new RSTile(3296, 3275),
						new RSTile(3296, 3274), new RSTile(3295, 3272),
						new RSTile(3294, 3270), new RSTile(3294, 3269),
						new RSTile(3294, 3267), new RSTile(3292, 3265),
						new RSTile(3293, 3262), new RSTile(3294, 3260),
						new RSTile(3293, 3258), new RSTile(3294, 3255),
						new RSTile(3294, 3253), new RSTile(3293, 3250),
						new RSTile(3286, 3238), new RSTile(3279, 3226),
						new RSTile(3278, 3213), new RSTile(3275, 3200),
						new RSTile(3281, 3191), new RSTile(3300, 3198),
						new RSTile(3298, 3198), new RSTile(3295, 3198),
						new RSTile(3280, 3196), new RSTile(3281, 3192),
						new RSTile(3281, 3189), new RSTile(3283, 3185),
						new RSTile(3281, 3184), new RSTile(3279, 3181),
						new RSTile(3276, 3180), new RSTile(3278, 3177),
						new RSTile(3276, 3173), new RSTile(3275, 3168),
						new RSTile(3274, 3167), new RSTile(3270, 3168),
						new RSTile(3269, 3168) };
				RSTile bank = new RSTile(3269, 3167);
				RSTile mine = new RSTile(3301, 3310);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 11933 };
				Rocks = mineRock;
			}
			if (jTree1.getLastSelectedPathComponent().toString()
					.contains("Al Kharid [Gold]")) {
				RSTile[] path = { new RSTile(3303, 3312),
						new RSTile(3301, 3312), new RSTile(3299, 3311),
						new RSTile(3298, 3307), new RSTile(3299, 3306),
						new RSTile(3298, 3301), new RSTile(3298, 3299),
						new RSTile(3299, 3295), new RSTile(3300, 3291),
						new RSTile(3299, 3288), new RSTile(3298, 3284),
						new RSTile(3297, 3282), new RSTile(3295, 3280),
						new RSTile(3296, 3276), new RSTile(3296, 3275),
						new RSTile(3296, 3274), new RSTile(3295, 3272),
						new RSTile(3294, 3270), new RSTile(3294, 3269),
						new RSTile(3294, 3267), new RSTile(3292, 3265),
						new RSTile(3293, 3262), new RSTile(3294, 3260),
						new RSTile(3293, 3258), new RSTile(3294, 3255),
						new RSTile(3294, 3253), new RSTile(3293, 3250),
						new RSTile(3286, 3238), new RSTile(3279, 3226),
						new RSTile(3278, 3213), new RSTile(3275, 3200),
						new RSTile(3281, 3191), new RSTile(3300, 3198),
						new RSTile(3298, 3198), new RSTile(3295, 3198),
						new RSTile(3280, 3196), new RSTile(3281, 3192),
						new RSTile(3281, 3189), new RSTile(3283, 3185),
						new RSTile(3281, 3184), new RSTile(3279, 3181),
						new RSTile(3276, 3180), new RSTile(3278, 3177),
						new RSTile(3276, 3173), new RSTile(3275, 3168),
						new RSTile(3274, 3167), new RSTile(3270, 3168),
						new RSTile(3269, 3168) };
				RSTile bank = new RSTile(3269, 3167);
				RSTile mine = new RSTile(3301, 3310);
				toMine = walking.newTilePath(reversePath(path));
				toBank = walking.newTilePath(path);
				BankTile = bank;
				MineTile = mine;
				// toBank = path;
				// toMine = walking.reversePath(path);
				int[] mineRock = { 37312, 37310 };
				Rocks = mineRock;
			}
			if (jTree1.getSelectionPath().toString().contains("Powermining")) {
				PowerMine = true;
			}
			if (jComboBox1.getSelectedItem().toString().contains("None")) {
				paint = false;
			}
			if (jComboBox1.getSelectedItem().toString().contains("Safe Paint")) {
				paint = false;
				safepaint = true;
			}
			if (jCheckBox1.isSelected()) {
				rest = true;
			}
			setVisible(false);
			Start = true;
			Settings = true;
		}

		// Variables declaration - do not modify
		private javax.swing.JButton jButton1;
		private javax.swing.JCheckBox jCheckBox1;
		private javax.swing.JComboBox jComboBox1;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JLabel jLabel3;
		private javax.swing.JLabel jLabel4;
		private javax.swing.JScrollPane jScrollPane1;
		private javax.swing.JSlider jSlider1;
		private javax.swing.JTree jTree1;
		// End of variables declaration

	}
}