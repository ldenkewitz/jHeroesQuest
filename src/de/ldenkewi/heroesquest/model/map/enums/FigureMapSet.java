package de.ldenkewi.heroesquest.model.map.enums;

/**
 * Set of textures of the figure map
 * @author Lars Denkewitz
 *
 */
public enum FigureMapSet {
//	  			  ThumbName		  AtkSound		  HitSound	  	DeathSound	 Health	Atk	Def Mov	IsHero
	Barbar 		("barbar.jpg"	, "sword1.wav",   "human.wav", 	"human.wav",   9, 	4, 	3, 	2,	true),
	Zwerg		("dwarf.jpg"	, "sword2.wav",   "human.wav", 	"human.wav",   9, 	3, 	4, 	2,	true),
	Goblin		("goblin.jpg"	, "sword3.wav",   "monster.wav", "monster.wav",1, 	1, 	1, 	2,	false),
	Orc			("orc.jpg"		, "monster.wav",  "monster.wav", "monster.wav",2, 	2, 	2, 	1,	false),
	Skelett		("sceleton.jpg"	, "monster2.wav", "monster.wav", "undead.wav", 1, 	2, 	1, 	2,	false),
	Mumie		("mummy.jpg"	, "monster2.wav", "monster.wav", "undead.wav", 2, 	2, 	3, 	1,	false),
	Boss		("boss.jpg"		, "sword1.wav",   "human.wav", 	"human2.wav",  3, 	3, 	3, 	2,	false),
	ChaosRitter	("chaos.jpg"	, "sword2.wav",   "human.wav", 	"human2.wav",  2, 	2, 	2, 	1,	false);
	
	private String thumbGraphic, atkSoundName, hitSoundName, deathSoundName;
	private int health, attackDices, defenseDices, moveDices;
	private boolean isHero;
	
	private FigureMapSet(String thumbName, String atkSound, String hitSound, String deathSound, 
			int health, int atk, int def, int move, boolean isHero) {
		this.thumbGraphic 	= thumbName;
		this.atkSoundName 	= atkSound;
		this.hitSoundName 	= hitSound;
		this.deathSoundName = deathSound;
		this.health 		= health;
		this.attackDices 	= atk;
		this.defenseDices 	= def;
		this.moveDices 		= move;
		this.isHero 		= isHero;
	}
	
	/**
	 * Returns the value of the attribute {@link #thumbGraphic}.
	 * @return the file name
	 */
	public String getThumbGraphic(){
		return thumbGraphic;
	}
	
	/** Returns the value of the attribute {@link #atkSoundName}.
	 * @return the atkSoundName
	 */
	public String getAtkSoundName() {
		return atkSoundName;
	}

	/** Returns the value of the attribute {@link #hitSoundName}.
	 * @return the hitSoundName
	 */
	public String getHitSoundName() {
		return hitSoundName;
	}

	/** Returns the value of the attribute {@link #deathSoundName}.
	 * @return the deathSoundName
	 */
	public String getDeathSoundName() {
		return deathSoundName;
	}

	/** Returns the value of the attribute {@link #health}.
	 * @return the health
	 */
	public int getHealth() {
		return health;
	}

	/** Returns the value of the attribute {@link #attackDices}.
	 * @return the attackDices
	 */
	public int getAttackDices() {
		return attackDices;
	}

	/** Returns the value of the attribute {@link #defenseDices}.
	 * @return the defenseDices
	 */
	public int getDefenseDices() {
		return defenseDices;
	}

	/** Returns the value of the attribute {@link #moveDices}.
	 * @return the moveDices
	 */
	public int getMoveDices() {
		return moveDices;
	}

	/** Returns the value of the attribute {@link #isHero}.
	 * @return the isHero
	 */
	public boolean isHero() {
		return isHero;
	}
}
