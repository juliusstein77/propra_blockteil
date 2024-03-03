Clazz.declarePackage("J.modelkit");
Clazz.load(["J.popup.PopupResource"], "J.modelkit.ModelKitPopupResourceBundle", ["J.i18n.GT", "J.modelkit.ModelKit"], function(){
var c$ = Clazz.declareType(J.modelkit, "ModelKitPopupResourceBundle", J.popup.PopupResource);
Clazz.overrideMethod(c$, "getMenuName", 
function(){
return "modelkitMenu";
});
Clazz.overrideMethod(c$, "buildStructure", 
function(menuStructure){
this.addItems(J.modelkit.ModelKitPopupResourceBundle.menuContents);
this.addItems(J.modelkit.ModelKitPopupResourceBundle.structureContents);
if (menuStructure != null) this.setStructure(menuStructure,  new J.i18n.GT());
}, "~S");
Clazz.overrideMethod(c$, "getWordContents", 
function(){
var wasTranslating = J.i18n.GT.setDoTranslate(true);
var words =  Clazz.newArray(-1, ["atomMenu", "<atoms.png>", "moreAtomMenu", "<dotdotdot.png>", "bondMenu", "<bonds.png>", "optionsMenu", "<dotdotdot.png>", "operator", "(no operator selected)", "xtalMenu", "<xtal.png>", "xtalModePersistMenu", "mode", "xtalSelPersistMenu", "select atom or position", "xtalSelOpPersistMenu", "select operator", "xtalOp!PersistMenu", "from list...", "xtalEditOptPersistMenu", "edit options", "xtalOptionsPersistMenu", "more options...", "xtalPackingPersistMenu", "packing", "mkshowSymopInfoCB", "show symmetry operator info", "mkaddHydrogensCB", "add hydrogens on new atoms", "mkclicktosetelementCB", "allow clicking to set atom element", "mkselop_byop", "from list", "mkselop_addOffset", "add/remove lattice offset", "mkselop_atom2", "to second atom", "mksel_atom", "select atom", "mksel_position", "select position", "mkmode_molecular", J.i18n.GT.$("disabled"), "mkmode_view", J.i18n.GT.$("View"), "mkmode_edit", J.i18n.GT.$("Edit"), "mksymmetry_none", J.i18n.GT.$("do not apply"), "mksymmetry_retainLocal", J.i18n.GT.$("retain local"), "mksymmetry_applyLocal", J.i18n.GT.$("apply local"), "mksymmetry_applyFull", J.i18n.GT.$("apply full"), "mkunitcell_extend", J.i18n.GT.$("extend cell"), "mkunitcell_packed", J.i18n.GT.$("pack cell"), "mkasymmetricUnit", J.i18n.GT.$("asymmetric unit"), "mkallAtoms", J.i18n.GT.$("all atoms"), "new", J.i18n.GT.$("new"), "undo", J.i18n.GT.$("undo (CTRL-Z)"), "redo", J.i18n.GT.$("redo (CTRL-Y)"), "center", J.i18n.GT.$("center"), "addh", J.i18n.GT.$("add hydrogens"), "minimize", J.i18n.GT.$("minimize"), "hmin", J.i18n.GT.$("fix hydrogens and minimize"), "clearQPersist", J.i18n.GT.$("clear"), "SIGNEDsaveFile", J.i18n.GT.$("save file"), "SIGNEDsaveState", J.i18n.GT.$("save state"), "invertStereoP!RD", J.modelkit.ModelKit.getText("invStereo"), "assignAtom_XP!RD", J.modelkit.ModelKit.getText("delAtom"), "assignAtom_XxP!RD", J.modelkit.ModelKit.getText("dragBond"), "dragAtomP!RD", J.modelkit.ModelKit.getText("dragAtom"), "dragMinimizeP!RD", J.modelkit.ModelKit.getText("dragMinimize"), "dragMoleculeP!RD", J.modelkit.ModelKit.getText("dragMolecule"), "dragMinimizeMoleculeP!RD", J.modelkit.ModelKit.getText("dragMMol"), "assignAtom_CP!RD", "C", "assignAtom_HP!RD", "H", "assignAtom_NP!RD", "N", "assignAtom_OP!RD", "O", "assignAtom_FP!RD", "F", "assignAtom_ClP!RD", "Cl", "assignAtom_BrP!RD", "Br", "_??P!RD", "??", "assignAtom_plP!RD", J.modelkit.ModelKit.getText("incCharge"), "assignAtom_miP!RD", J.modelkit.ModelKit.getText("decCharge"), "assignBond_0P!RD", J.modelkit.ModelKit.getText("bondTo0"), "assignBond_1P!RD", J.modelkit.ModelKit.getText("bondTo1"), "assignBond_2P!RD", J.modelkit.ModelKit.getText("bondTo2"), "assignBond_3P!RD", J.modelkit.ModelKit.getText("bondTo3"), "assignBond_pP!RD", J.modelkit.ModelKit.getText("incBond"), "assignBond_mP!RD", J.modelkit.ModelKit.getText("decBond"), "rotateBondP!RD", J.modelkit.ModelKit.getText("rotBond"), "exit!Persist", J.i18n.GT.$("exit modelkit mode")]);
J.i18n.GT.setDoTranslate(wasTranslating);
return words;
});
Clazz.overrideMethod(c$, "getMenuAsText", 
function(title){
return this.getStuctureAsText(title, J.modelkit.ModelKitPopupResourceBundle.menuContents, J.modelkit.ModelKitPopupResourceBundle.structureContents);
}, "~S");
c$.menuContents =  Clazz.newArray(-1, [ Clazz.newArray(-1, ["modelkitMenu", "atomMenu bondMenu xtalMenu optionsMenu"]),  Clazz.newArray(-1, ["optionsMenu", "new center addh minimize hmin  - undo redo - SIGNEDsaveFile SIGNEDsaveState exit!Persist"]),  Clazz.newArray(-1, ["atomMenu", "assignAtom_XxP!RD dragAtomP!RD dragMinimizeP!RD dragMoleculeP!RD dragMinimizeMoleculeP!RD invertStereoP!RD -  assignAtom_XP!RD assignAtom_CP!RD assignAtom_HP!RD assignAtom_NP!RD assignAtom_OP!RD assignAtom_FP!RD assignAtom_ClP!RD assignAtom_BrP!RD _??P!RD _??P!RD _??P!RD moreAtomMenu assignAtom_plP!RD assignAtom_miP!RD"]),  Clazz.newArray(-1, ["moreAtomMenu", "clearQPersist , _??P!RD _??P!RD _??P!RD _??P!RD _??P!RD _??P!RD "]),  Clazz.newArray(-1, ["bondMenu", "assignBond_0P!RD assignBond_1P!RD assignBond_2P!RD assignBond_3P!RD assignBond_pP!RD assignBond_mP!RD rotateBondP!RD"]),  Clazz.newArray(-1, ["xtalMenu", "xtalModePersistMenu xtalSelPersistMenu xtalSelOpPersistMenu operator xtalPackingPersistMenu xtalEditOptPersistMenu xtalOptionsPersistMenu"]),  Clazz.newArray(-1, ["xtalModePersistMenu", "mkmode_molecular mkmode_view mkmode_edit"]),  Clazz.newArray(-1, ["xtalSelPersistMenu", "mksel_atom mksel_position"]),  Clazz.newArray(-1, ["xtalSelOpPersistMenu", "xtalOp!PersistMenu mkselop_atom2 mkselop_addOffset"]),  Clazz.newArray(-1, ["xtalEditOptPersistMenu", "mksymmetry_none mksymmetry_retainLocal mksymmetry_applyLocal mksymmetry_applyFull"]),  Clazz.newArray(-1, ["xtalPackingPersistMenu", "mkunitcell_packed mkunitcell_extend"]),  Clazz.newArray(-1, ["xtalOptionsPersistMenu", "mkshowSymopInfoCB mkclicktosetelementCB mkaddHydrogensCB"])]);
c$.structureContents =  Clazz.newArray(-1, [ Clazz.newArray(-1, ["new", "zap"]),  Clazz.newArray(-1, ["center", "zoomto 0 {visible} 0/1.5"]),  Clazz.newArray(-1, ["addh", "calculate hydrogens {model=_lastframe}"]),  Clazz.newArray(-1, ["minimize", "minimize"]),  Clazz.newArray(-1, ["hmin", "delete hydrogens and model=_lastframe; minimize addhydrogens"]),  Clazz.newArray(-1, ["SIGNEDsaveFile", "select visible;write COORD '?jmol.mol'"]),  Clazz.newArray(-1, ["SIGNEDsaveState", "write '?jmol.jpg'"]),  Clazz.newArray(-1, ["clearQ", "clearQ"]),  Clazz.newArray(-1, ["undo", "!undoMove"]),  Clazz.newArray(-1, ["redo", "!redoMove"]),  Clazz.newArray(-1, ["operator", ""]),  Clazz.newArray(-1, ["exit!Persist", "set modelkitMode false"])]);
});
;//5.0.1-v2 Tue Feb 20 10:58:47 CST 2024
