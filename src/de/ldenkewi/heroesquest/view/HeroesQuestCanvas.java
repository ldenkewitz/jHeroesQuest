package de.ldenkewi.heroesquest.view;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.*;
import javax.swing.JPanel;
import javax.vecmath.*;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import de.ldenkewi.heroesquest.controll.ViewCtrl;
import de.ldenkewi.heroesquest.model.*;
import de.ldenkewi.heroesquest.model.map.enums.ObjectMapSet;
import de.ldenkewi.heroesquest.view.listeners.GlobalKeyboardListener;

/**
 * The Java3D class, that contains the whole Java3D code (universe) and extends JPanel.
 * @author Lars Denkewitz
 * @version from 14/04/2009
 */

// SceneGraph : scene						-ChildLevel-
//
//			     0 root BranchGroup				-0-
//				 |\
//				 | 0 bg Background
//				 0 mouseTG TransformGroup		-1-
//			    / \
//			   /   0 mr MouseRotate
//			  /  
//		     0 tgFloor TransformGroup				-2-
//			/|\
//		   / | 0 tgField TransformGroup			-3-
//		  /	 |  \
//		 |   |   0 box Box
//	    /|	 0 bgFigure BranchGroup				-3-
//	   / |	  \
//	  /	 |	   0 tgFigure TransformGroup
//	 / 	 |		\
//	/  	 |		 0 tgRot TransformGroup
//  |  	 |		  \
//  |  	 |		   0 cyl Cylinder
//  |	 0 bgItem BranchGroup					-3-
//  |      \
//  |       0 tgItem TransformGroup
//  |        \
//  |         0 box Box
//  0 bgDoor BranchGroup						-3-
//   \
//    0 tgDoor TransformGroup
//	   \
//	    0 box Box

public class HeroesQuestCanvas extends JPanel {
	private static final long serialVersionUID = 1L;
	private final static String RESSOURCES_JPG_DOOR		= ViewCtrl.RESSOURCES_GRAPHICS_MAPITEMS + "door.jpg";
	private final static String RESSOURCES_JPG_SITE 	= ViewCtrl.RESSOURCES_GRAPHICS_GROUND + "site.jpg";
	private final static String RESSOURCES_JPG_CASTLE 	= ViewCtrl.RESSOURCES_GRAPHICS_RANDOM + "castle.jpg";

	private final static float 	fieldLength = 0.3f;
	private final static float 	fieldHeight = 0.03f;
	
	private int 				sizeX, sizeY;
	private File				backgroundFile = new File(RESSOURCES_JPG_CASTLE);
	private TransformGroup 		tgFloor;
	private BranchGroup 		scene; 
	private SimpleUniverse		simpleU;
	private List<Figure> 		figures;
	private List<Cylinder>		figuresCyl; // 3D-Objekte der jeweiligen Figuren -> gleicher Listenindex
	private BoundingSphere 		sphere = new BoundingSphere(new Point3d(0, 0, 0), 100);
	/**
	 * Constructor.
	 * @param bounds Bound of the panel
	 * @param fields {@link java.util.ArrayList} of {@link Field}
	 * @param sizeX	Size of the playing field in {@link Field}s on x-axis
	 * @param sizeY Size of the playing field in {@link Field}s on y-axis
	 */
	public HeroesQuestCanvas(Rectangle bounds, ArrayList<Field> fields, int sizeX, int sizeY,
			File backgroundFile) {
		
		setBounds(bounds);
		init(fields, sizeX, sizeY, backgroundFile);
	}
	
	/** Initializing the object.
	 * @param fields
	 * @param sizeX
	 * @param sizeY
	 * @param backgroundFile
	 */
	private void init(ArrayList<Field> fields, int sizeX, int sizeY, File backgroundFile) {
		this.sizeX 		= sizeX;
		this.sizeY 		= sizeY;
		this.tgFloor 	= createFloor(fields);
		this.figures 	= new ArrayList<Figure>();
		this.figuresCyl = new ArrayList<Cylinder>();
		if(backgroundFile != null)
			this.backgroundFile = backgroundFile;
		
		this.scene 		= createSceneGraph(tgFloor);
		this.setRequestFocusEnabled(false);
		createUniverse(scene);
	}

	private void createUniverse(BranchGroup scene) {
		Canvas3D canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
		canvas3D.setBounds(this.getBounds());
		canvas3D.addKeyListener(GlobalKeyboardListener.getInstance());
		add(canvas3D);
		
		simpleU = new SimpleUniverse(canvas3D);
		simpleU.getViewingPlatform().setNominalViewingTransform();
		// BranchGroup scene = this.createSceneGraph();
		simpleU.addBranchGraph(scene);

		// Orbiter for Zoom
		OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ALL
		/* | OrbitBehavior.STOP_ZOOM */| OrbitBehavior.DISABLE_ROTATE );
		orbit.setSchedulingBounds(new BoundingSphere(new Point3d(), 100));
		orbit.setMinRadius(0);
		// orbit.setRotFactors(0, 0.08);
		simpleU.getViewingPlatform().setViewPlatformBehavior(orbit);
	}
	
	/**
	 * Creates the BranchGroup SceneGraph.
	 * @param tgFloor The TransformGroup that is added as child to the SceneGraph
	 * @return Returns the BranchGroup root
	 */
	private BranchGroup createSceneGraph(TransformGroup floor) {
		Transform3D t3d = new Transform3D();
		Transform3D t3dAxis = new Transform3D();

		t3d.rotX(Math.toRadians(35)); // 35
		t3dAxis.rotY(Math.toRadians(45)); // 45
		t3d.mul(t3dAxis);
		t3d.setTranslation(new Vector3f(-fieldLength * sizeX, 0.0f, -sizeY * 0.45f));

		BranchGroup root = new BranchGroup();
		TransformGroup tgMouse = new TransformGroup(t3d);
		
		// light is actually not needed
//		AmbientLight light = new AmbientLight(true, new Color3f(Color.WHITE));
//		light.setInfluencingBounds(sphere);
//		root.addChild(light);

		tgMouse.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		tgMouse.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		// Rotate
		MouseRotate mr = new MouseRotate(tgMouse);
		mr.setSchedulingBounds(sphere);
		mr.setFactor(0, 0.001);
		tgMouse.addChild(mr);
		
		// Zoom - function supported by orbiter
//		MouseZoom mz = new MouseZoom(mouseTG);
//		mz.setSchedulingBounds(sphere);
//		mouseTG.addChild(mz);
		
		// Translate - function supported by orbiter
//		MouseTranslate mt = new MouseTranslate(mouseTG);
//		mt.setSchedulingBounds(sphere);
//		mouseTG.addChild(mt);

		// mouseTG.addChild(createFloor("graphics/test.jpg"));
		tgMouse.addChild(floor);
		root.addChild(tgMouse);

		Background bg = new Background(new TextureLoader(backgroundFile.getPath(), this).getImage());
		bg.setImageScaleMode(Background.SCALE_FIT_ALL);
		bg.setApplicationBounds(sphere);
		root.addChild(bg);
		root.setCapability(BranchGroup.ALLOW_DETACH);

		return root;
	}


	/**
	 * 	/**
	 * Creates the TransFormgroup of the (ground) tgFloor.
	 * @param fields ArrayList of Fields that are used for the tgFloor
	 * @return Returns the TransformGroup tgFloor
	 */
	private TransformGroup createFloor(ArrayList<Field> fields) {
		TransformGroup floor = new TransformGroup();
		Transform3D transform3D = new Transform3D();
		TextureAttributes txa = new TextureAttributes();

		// Texture Top
		Material ma = new Material(new Color3f(0.0f, 0.0f, 0.0f), new Color3f(0.7f, 0.7f, 0.7f),
				new Color3f(1.0f, 1.0f, 1.0f), new Color3f(0.4f, 0.4f, 0.4f), 16.0f);
		txa.setTextureMode(TextureAttributes.COMBINE);
		Appearance app[] = new Appearance[8];
		for (int i = 0; i < 8; i++) {
			app[i] = new Appearance();
			app[i].setTexture(new TextureLoader(ViewCtrl.RESSOURCES_GRAPHICS_GROUND+"ground" + i + ".jpg", this)
					.getTexture());
			app[i].setMaterial(ma);
			app[i].setTextureAttributes(txa);
		}

		// Texture Site
		Appearance appSite = new Appearance();
		Texture txSite = new TextureLoader(RESSOURCES_JPG_SITE, this).getTexture();
		appSite.setTexture(txSite);

		// Texture Bottom
		Appearance appBottom = new Appearance();
		Texture txBottom = new TextureLoader(ViewCtrl.RESSOURCES_GRAPHICS_GROUND+"bottom.jpg", this).getTexture();
		appBottom.setTexture(txBottom);
		// appBottom.setColoringAttributes(new ColoringAttributes(new Color3f(Color.GRAY),
		// ColoringAttributes.FASTEST));

		// Felder setzen
		for (Field field : fields) {
			Box box = new Box(fieldLength, fieldHeight, fieldLength, Box.GENERATE_TEXTURE_COORDS
					| Box.GENERATE_NORMALS, new Appearance());

			box.getShape(Box.TOP).setAppearance(app[field.getFieldNumber()]);
			box.getShape(Box.LEFT).setAppearance(appSite);
			box.getShape(Box.RIGHT).setAppearance(appSite);
			box.getShape(Box.FRONT).setAppearance(appSite);
			box.getShape(Box.BACK).setAppearance(appSite);
			box.getShape(Box.BOTTOM).setAppearance(appBottom);

			box.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			transform3D.setTranslation(new Vector3d(2 * field.getX() * fieldLength, fieldHeight, 2
					* field.getY() * fieldLength));
			TransformGroup tgField = new TransformGroup(transform3D);
			tgField.addChild(box);
			floor.addChild(tgField);
		}

		floor.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		floor.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);

		return floor;
	}

	/**
	 * Creates a BranchGroup that includes at the end a {@link com.sun.j3d.utils.geometry.Cylinder Cylinder} that symbolizes a figure on the playing field. <BR>
	 * This BranchGroup is added as child to the TransformGroup tgFloor, the child of the SceneGraph.
	 * @param figure Figure that is added to the playing field as cylinder
	 */
	public void setFigure(Figure figure) {

		BranchGroup 	bgFigure 		= new BranchGroup();
		TransformGroup 	tgFigure, tgRot 	= new TransformGroup();
		Transform3D 	transform3D 	= new Transform3D();
		Cylinder 		cyl;
		tgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		Appearance appSite 	= new Appearance();
		appSite.setCapability(Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
		Appearance appTop 	= new Appearance();
		appTop.setTexture(new TextureLoader(ViewCtrl.RESSOURCES_GRAPHICS_FIGURES_CANVAS + figure.getTextureFileName(), this)
				.getTexture());

		if (figure.isHero()) {
			appSite.setColoringAttributes(new ColoringAttributes(new Color3f(new Color(38, 127, 0)),
					ColoringAttributes.FASTEST));
		} else {
			appSite.setColoringAttributes(new ColoringAttributes(new Color3f(new Color(110, 0, 0)),
					ColoringAttributes.FASTEST));
		}
		cyl = new Cylinder(fieldLength, fieldHeight*2, Cylinder.GENERATE_TEXTURE_COORDS
				| Cylinder.GENERATE_NORMALS, 20, 20, new Appearance());
		cyl.getShape(Cylinder.TOP).setAppearance(appTop);
		cyl.getShape(Cylinder.BODY).setAppearance(appSite);
		cyl.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

		transform3D.setTranslation(new Vector3d(2 * figure.getX() * fieldLength, fieldHeight * 3, 2
				* figure.getY() * fieldLength));

		tgFigure = new TransformGroup(transform3D);
		tgFigure.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tgRot.addChild(cyl);
		tgFigure.addChild(tgRot);
		bgFigure.addChild(tgFigure);
		bgFigure.setCapability(BranchGroup.ALLOW_DETACH);

		tgFloor.addChild(bgFigure);

		this.figuresCyl.add(cyl);
		this.figures.add(figure);
		
		changeLineOfSight(figure);
	}

	/**
	 * Creates a BranchGroup that includes at the end a {@link com.sun.j3d.utils.geometry.Box Box} that symbolizes a mapItem on the playing field. <BR>
	 * This BranchGroup is added as child to the TransformGroup tgFloor, the child of the SceneGraph.
	 * @param mapItemImpl Figure that is added to the playing field as box
	 */
	public void setMapItemImpl(MapItemImpl mapItemImpl) {
		BranchGroup 	bgItem 		= new BranchGroup();
		Transform3D 	transform3D = new Transform3D();
		TransformGroup 	tgItem;
		
		Appearance appSite = new Appearance();
		Appearance appTop = new Appearance();
		appTop.setTexture(new TextureLoader(ViewCtrl.RESSOURCES_GRAPHICS_MAPITEMS + mapItemImpl.getTextureFileName(), this)
				.getTexture());

		
		appSite.setColoringAttributes(new ColoringAttributes(new Color3f(Color.GRAY), ColoringAttributes.FASTEST));
		Box box = null;
		if(mapItemImpl.getTextureFileName().equals(ObjectMapSet.TABLE.getFileName())) {
			box = new Box(fieldLength, fieldHeight, fieldLength*0.6f, Box.GENERATE_TEXTURE_COORDS
				| Box.GENERATE_NORMALS, appSite);
		} else {
			box = new Box(fieldLength, fieldHeight, fieldLength, Box.GENERATE_TEXTURE_COORDS
					| Box.GENERATE_NORMALS, appSite);
		}
		box.getShape(Box.TOP).setAppearance(appTop);

		transform3D.setTranslation(new Vector3d(2 * mapItemImpl.getX() * fieldLength, fieldHeight * 2, 2
				* mapItemImpl.getY() * fieldLength));

		tgItem = new TransformGroup(transform3D);

		tgItem.addChild(box);
		bgItem.addChild(tgItem);

		tgFloor.addChild(bgItem);
	}

	/**
	 * Creates a BranchGroup that includes at the end a {@link com.sun.j3d.utils.geometry.Box Box} that symbolizes a door on the playing field. <BR>
	 * This BranchGroup is added as child to the TransformGroup tgFloor, the child of the SceneGraph. <BR>
	 * The door box lies between two neighbored fields.
	 * @param x1 X-position of the first field
	 * @param y1 Y-position of the first field
	 * @param x2 X-position of the second field
	 * @param y2 Y-position of the second field
	 */
	public void setDoor(int x1, int y1, int x2, int y2) {
		BranchGroup 	bgDoor 		= new BranchGroup();
		Transform3D 	transform3D = new Transform3D();
		Box 			box 		= null;
		TransformGroup 	tgDoor;

		Appearance appSite 	= new Appearance();
		Appearance appFront = new Appearance();
		appFront.setTexture(new TextureLoader(RESSOURCES_JPG_DOOR, this).getTexture());
		appSite.setColoringAttributes(new ColoringAttributes(new Color3f(Color.GRAY), ColoringAttributes.FASTEST));
		
		if(x1 == x2){
			box = new Box(fieldLength, fieldLength, fieldHeight, Box.GENERATE_TEXTURE_COORDS
					| Box.GENERATE_NORMALS, appSite);
			box.getShape(Box.BACK).setAppearance(appFront);
			box.getShape(Box.FRONT).setAppearance(appFront);
			
			transform3D.setTranslation(new Vector3d(2 * x2 * fieldLength, fieldLength + fieldHeight * 2, 2
					* y1 * fieldLength - fieldLength));
		} else {
			box = new Box(fieldHeight, fieldLength, fieldLength, Box.GENERATE_TEXTURE_COORDS
					| Box.GENERATE_NORMALS, appSite);
			box.getShape(Box.LEFT).setAppearance(appFront);
			box.getShape(Box.RIGHT).setAppearance(appFront);
			
			transform3D.setTranslation(new Vector3d(2 * x1 * fieldLength - fieldLength, fieldLength + fieldHeight * 2,
					2 * y2 * fieldLength));
		}
		
		tgDoor = new TransformGroup(transform3D);
		
		tgDoor.addChild(box);
		bgDoor.addChild(tgDoor);

		tgFloor.addChild(bgDoor); 
	}
	
	/**
	 * Changes the color of the cylinder to blue, that stands for the figure, given by its id. <BR>
	 * It also changes all other cylinders color to its default: green = hero, red = monster. 
	 * @param id ID of the active figure to change its color
	 */
	public void setActiveFigure(int id) {
		int index = 0;
		for (Figure f : figures) {
			if(f.getId() == id) {				
				Cylinder cyl = figuresCyl.get(index);
				Appearance ap = cyl.getAppearance();
				ap.setColoringAttributes(new ColoringAttributes(new Color3f(new Color(40, 50, 200)),
						ColoringAttributes.FASTEST));
			}else{
				if(f.isHero()) {
					Cylinder cyl = figuresCyl.get(index);
					Appearance ap = cyl.getAppearance();
					ap.setColoringAttributes(new ColoringAttributes(new Color3f(new Color(38, 127, 0)),
							ColoringAttributes.FASTEST));
				}else{
					Cylinder cyl = figuresCyl.get(index);
					Appearance ap = cyl.getAppearance();
					ap.setColoringAttributes(new ColoringAttributes(new Color3f(new Color(110, 0, 0)),
							ColoringAttributes.FASTEST));
				}
			}
			index++;
		}
	}
	
	/**
	 * Turns the rotation (TransformGroup) to the line of sight of the given figure.
	 * @param figure Figure that should be rotated
	 */
	public void changeLineOfSight(Figure figure) {
		int index = 0;
		for(Figure f : figures) {
			if(figure.getId() == f.getId()) {
				f = figure;
				Cylinder cyl = figuresCyl.get(index);
				TransformGroup tg = (TransformGroup)cyl.getParent();
				Transform3D transform3D = new Transform3D();
				
				transform3D.rotY(Math.toRadians(f.getDirection()*90));
				tg.setTransform(transform3D);
				break;
			}
			index++;
		}
	}

	/**
	 * Moves the cylinder of the given figure to its actual position.
	 * @param figure Figure to be moved
	 */
	public void moveFigure(Figure figure) {
		int index = 0;
		for(Figure f : figures) {
			if(figure.getId() == f.getId()) {
				Cylinder cyl = figuresCyl.get(index);
				TransformGroup rot = (TransformGroup)cyl.getParent();
				TransformGroup tg = (TransformGroup)rot.getParent();
				Transform3D transform3D = new Transform3D();
				
				transform3D.setTranslation(new Vector3d(2 * figure.getX() * fieldLength, fieldHeight * 3, 2
							* figure.getY() * fieldLength));
				
				tg.setTransform(transform3D);
				break;
			}
			index++;
		}
	}

	/**
	 * Removes the hole BranchGroup, that stands for the given figure. <BR>
	 * It also deletes the figure in {@link #figures}.
	 * @param figure Figure to be deleted 
	 */
	public void deleteFigure(Figure figure) {
		int index = this.figures.indexOf(figure);
		this.figures.remove(figure);
		
		Cylinder cyl = figuresCyl.get(index);
		TransformGroup rot = (TransformGroup)cyl.getParent();
		TransformGroup tg = (TransformGroup)rot.getParent();
		BranchGroup bg = (BranchGroup)tg.getParent();
		TransformGroup floor = (TransformGroup)bg.getParent();
		
		floor.removeChild(bg);
		figuresCyl.remove(index);
	}
	
	public void clearCanvas() {
		tgFloor = null;
		scene 	 = null;
		simpleU.removeAllLocales();
		simpleU = null;
		figures.clear(); 	figures 	= null;
		figuresCyl.clear(); figuresCyl  = null;
	}

	/*
	// here the start for picking figures from playing field with mouse  
	class MyPickingBehaviour extends PickMouseBehavior {
		public MyPickingBehaviour(Canvas3D canvas, BranchGroup root, Bounds bounds) {
			super(canvas, root, bounds);
			setSchedulingBounds(bounds);
		}

		@Override
		public void updateScene(int xpos, int ypos) {
			Primitive pickShape = null;
			pickCanvas.setShapeLocation(xpos, ypos);
			PickResult pickResult = pickCanvas.pickClosest();
			if (pickResult != null){
				pickShape = (Primitive) pickResult.getNode(PickResult.PRIMITIVE);
			}
			if (pickShape != null){
				Appearance ap = pickShape.getAppearance();
				Object data = pickShape.getUserData();
				System.out.println("pick!");
//				if (data instanceof ...) {
//					choosenElement = (...) data;
//					//setActive
//				}
			}
		}
	}
	*/
}
