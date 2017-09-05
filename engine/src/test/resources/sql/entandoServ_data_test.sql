INSERT INTO authgroups (groupname, descr) VALUES ('administrators', 'Amministratori');
INSERT INTO authgroups (groupname, descr) VALUES ('coach', 'Coach');
INSERT INTO authgroups (groupname, descr) VALUES ('customers', 'Customers');
INSERT INTO authgroups (groupname, descr) VALUES ('free', 'Accesso Libero');
INSERT INTO authgroups (groupname, descr) VALUES ('helpdesk', 'Helpdesk');
INSERT INTO authgroups (groupname, descr) VALUES ('management', 'Management');




INSERT INTO authroles (rolename, descr) VALUES ('admin', 'Tutte le funzioni');
INSERT INTO authroles (rolename, descr) VALUES ('editor', 'Gestore di Contenuti e Risorse');
INSERT INTO authroles (rolename, descr) VALUES ('supervisor', 'Supervisore di Contenuti');
INSERT INTO authroles (rolename, descr) VALUES ('pageManager', 'Gestore di Pagine');




INSERT INTO authpermissions (permissionname, descr) VALUES ('managePages', 'Operazioni su Pagine');
INSERT INTO authpermissions (permissionname, descr) VALUES ('enterBackend', 'Accesso all''Area di Amministrazione');
INSERT INTO authpermissions (permissionname, descr) VALUES ('manageResources', 'Operazioni su Risorse');
INSERT INTO authpermissions (permissionname, descr) VALUES ('editContents', 'Redazione di Contenuti');
INSERT INTO authpermissions (permissionname, descr) VALUES ('validateContents', 'Supervisione di Contenuti');
INSERT INTO authpermissions (permissionname, descr) VALUES ('superuser', 'Tutte le funzioni');
INSERT INTO authpermissions (permissionname, descr) VALUES ('manageCategories', 'Operazioni su Categorie');




INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('admin', 'superuser');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('pageManager', 'enterBackend');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('editor', 'enterBackend');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('supervisor', 'enterBackend');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('pageManager', 'managePages');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('supervisor', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('editor', 'editContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('supervisor', 'validateContents');
INSERT INTO authrolepermissions (rolename, permissionname) VALUES ('editor', 'manageResources');




INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('pageManagerCoach', 'coach', 'pageManager');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('pageManagerCustomers', 'customers', 'pageManager');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('supervisorCoach', 'coach', 'supervisor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('supervisorCustomers', 'customers', 'supervisor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('editorCoach', 'coach', 'editor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('editorCustomers', 'customers', 'editor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('supervisorCoach', 'customers', 'supervisor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('editorCoach', 'customers', 'editor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('mainEditor', 'administrators', 'editor');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('pageManagerCoach', 'customers', 'pageManager');
INSERT INTO authusergrouprole (username, groupname, rolename) VALUES ('admin', 'administrators', 'admin');




INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('supervisorCoach', 'supervisorCoach', '2008-09-25 00:00:00', '2009-01-30 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('mainEditor', 'mainEditor', '2008-09-25 00:00:00', '2009-01-30 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('pageManagerCoach', 'pageManagerCoach', '2008-09-25 00:00:00', '2009-01-30 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('supervisorCustomers', 'supervisorCustomers', '2008-09-25 00:00:00', '2009-01-30 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('pageManagerCustomers', 'pageManagerCustomers', '2008-09-25 00:00:00', '2009-01-30 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('editorCustomers', 'editorCustomers', '2008-09-25 00:00:00', '2009-07-02 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('editorCoach', 'editorCoach', '2008-09-25 00:00:00', '2009-07-02 00:00:00', NULL, 1);
INSERT INTO authusers (username, passwd, registrationdate, lastaccess, lastpasswordchange, active) VALUES ('admin', 'admin', '2008-09-25 00:00:00', '2009-12-16 00:00:00', NULL, 1);




INSERT INTO authuserprofiles (username, profiletype, profilexml, publicprofile) VALUES ('editorCustomers', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="editorCustomers" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories />
	<attributes>
		<attribute name="fullname" attributetype="Monotext"><monotext>Sean Red</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>19520521</date></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>sean.red@mailinator.com</monotext></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);
INSERT INTO authuserprofiles (username, profiletype, profilexml, publicprofile) VALUES ('mainEditor', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="mainEditor" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories />
	<attributes>
		<attribute name="fullname" attributetype="Monotext"><monotext>Amanda Chedwase</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>19471124</date></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>amanda.chedwase@mailinator.com</monotext></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);
INSERT INTO authuserprofiles (username, profiletype, profilexml, publicprofile) VALUES ('pageManagerCoach', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="pageManagerCoach" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories />
	<attributes>
		<attribute name="fullname" attributetype="Monotext"><monotext>Raimond Stevenson</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>20000904</date></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>raimond.stevenson@mailinator.com</monotext></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);
INSERT INTO authuserprofiles (username, profiletype, profilexml, publicprofile) VALUES ('editorCoach', 'PFL', '<?xml version="1.0" encoding="UTF-8"?>
<profile id="editorCoach" typecode="PFL" typedescr="Default user profile"><descr /><groups /><categories />
	<attributes>
		<attribute name="fullname" attributetype="Monotext"><monotext>Rick Bobonsky</monotext></attribute>
		<attribute name="email" attributetype="Monotext"><monotext>rick.bobonsky@mailinator.com</monotext></attribute>
		<attribute name="birthdate" attributetype="Date"><date>19450301</date></attribute>
		<attribute name="language" attributetype="Monotext"><monotext>it</monotext></attribute>
		<attribute name="boolean1" attributetype="Boolean"><boolean>false</boolean></attribute>
		<attribute name="boolean2" attributetype="Boolean"><boolean>false</boolean></attribute>
	</attributes>
</profile>', 0);

INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'fullname', 'Rick Bobonsky', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'email', 'rick.bobonsky@mailinator.com', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'birthdate', NULL, '1945-03-01 00:00:00', NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCoach', 'boolean2', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'fullname', 'Sean Red', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'email', 'sean.red@mailinator.com', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'birthdate', NULL, '1952-05-21 00:00:00', NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('editorCustomers', 'boolean2', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'fullname', 'Amanda Chedwase', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'email', 'amanda.chedwase@mailinator.com', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'birthdate', NULL, '1947-11-24 00:00:00', NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('mainEditor', 'boolean2', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'fullname', 'Raimond Stevenson', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'email', 'raimond.stevenson@mailinator.com', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'birthdate', NULL, '2000-09-04 00:00:00', NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'boolean1', 'false', NULL, NULL, NULL);
INSERT INTO authuserprofilesearch (username, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('pageManagerCoach', 'boolean2', 'false', NULL, NULL, NULL);




INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('editorCoach', 'fullname', 'userprofile:fullname');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('editorCustomers', 'fullname', 'userprofile:fullname');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('mainEditor', 'fullname', 'userprofile:fullname');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('pageManagerCoach', 'fullname', 'userprofile:fullname');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('editorCoach', 'email', 'userprofile:email');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('editorCustomers', 'email', 'userprofile:email');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('mainEditor', 'email', 'userprofile:email');
INSERT INTO authuserprofileattrroles (username, attrname, rolename) VALUES ('pageManagerCoach', 'email', 'userprofile:email');


INSERT INTO dataobjectmodels (modelid, datatype, descr, model, stylesheet) VALUES (2, 'ART', 'per test rendering
', '$content.id;
#foreach ($autore in $content.Autori)
$autore.text;
#end
$content.Titolo.getText();
$content.VediAnche.text,$content.VediAnche.destination;
$content.Foto.text,$content.Foto.imagePath("1");
$content.Data.mediumDate;

', NULL);
INSERT INTO dataobjectmodels (modelid, datatype, descr, model, stylesheet) VALUES (3, 'ART', 'scheda di un articolo', '------ RENDERING CONTENUTO: id = $content.id; ---------
ATTRIBUTI:
  - AUTORI (Monolist-Monotext):
#foreach ($autore in $content.Autori)
         testo=$autore.text;
#end
  - TITOLO (Text): testo=$content.Titolo.getText();
  - VEDI ANCHE (Link): testo=$content.VediAnche.text, dest=$content.VediAnche.destination;
  - FOTO (Image): testo=$content.Foto.text, src(1)=$content.Foto.imagePath("1");
  - DATA (Date): data_media = $content.Data.mediumDate;
------ END ------

', NULL);
INSERT INTO dataobjectmodels (modelid, datatype, descr, model, stylesheet) VALUES (1, 'ART', 'Main Model', '#if ($content.Titolo.text != "")<h1 class="titolo">$content.Titolo.text</h1>#end
#if ($content.Data.longDate != "")<p>Data: $content.Data.longDate</p>#end
$content.CorpoTesto.getTextBeforeImage(0)
#if ( $content.Foto.imagePath("2") != "" )
<img class="left" src="$content.Foto.imagePath("2")" alt="$content.Foto.text" />
#end
$content.CorpoTesto.getTextAfterImage(0)
#if ($content.Numero.number)<p>Numero: $content.Numero.number</p>#end
#if ($content.Autori && $content.Autori.size() > 0)
<h2 class="titolo">Autori:</h2>
<ul title="Authors">
#foreach ($author in $content.Autori)
	<li>$author.text;</li>
#end
</ul>
#end
#if ($content.VediAnche.text != "")
<h2 class="titolo">Link:</h2>
<p>
<li><a href="$content.VediAnche.destination">$content.VediAnche.text</a></li>
</p>
#end', NULL);
INSERT INTO dataobjectmodels (modelid, datatype, descr, model, stylesheet) VALUES (11, 'ART', 'List Model', '#if ($content.Titolo.text != "")<h1 class="titolo">$content.Titolo.text</h1>#end
<a href="$content.contentLink">Details...</a>', NULL);


INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART187', 'ART', 'una descrizione particolare', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART187" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione particolare</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', '20051012164415', '20060622194219', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART187" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione particolare</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN20', 'EVN', 'Mostra zootecnica', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN20" typecode="EVN" typedescr="Evento"><descr>Mostra zootecnica</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Mostra Zootecnica</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Annuncio svolgimento mostra zootecnicaMostra</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20060213</date></attribute><attribute name="DataFine" attributetype="Date"><date>20060220</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209100217', '20080209123357', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN20" typecode="EVN" typedescr="Evento"><descr>Mostra zootecnica</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Mostra Zootecnica</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Annuncio svolgimento mostra zootecnicaMostra</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20060213</date></attribute><attribute name="DataFine" attributetype="Date"><date>20060220</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN192', 'EVN', 'Evento 2', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN192" typecode="EVN" typedescr="Evento"><descr>Evento 2</descr><groups mainGroup="free" /><categories><category id="evento" /><category id="general_cat1" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo B - Evento 2</text><text lang="en">Title B - Event 2</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto evento 2</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19990414</date></attribute><attribute name="DataFine" attributetype="Date"><date>19990614</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20060418142303', '20061221161202', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN192" typecode="EVN" typedescr="Evento"><descr>Evento 2</descr><groups mainGroup="free" /><categories><category id="evento" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo B - Evento 2</text><text lang="en">Title B - Event 2</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto evento 2</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19990414</date></attribute><attribute name="DataFine" attributetype="Date"><date>19990614</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART179', 'ART', 'una descrizione ON LINE', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART179" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione ON LINE</descr><groups mainGroup="free" /><categories><category id="general_cat1" /><category id="general_cat2" /></categories><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20090716</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>', '20051012105533', '20080210180714', NULL, 'free', '0.1', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('RAH1', 'RAH', 'Articolo', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="RAH1" typecode="RAH" typedescr="Tipo_Semplice"><descr>Articolo</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Un bel titolo</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[test test]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="email" attributetype="Monotext" /><attribute name="Numero" attributetype="Number" /><attribute name="Correlati" attributetype="Link" /><attribute name="Allegati" attributetype="Attach"><resource resourcetype="Attach" id="7" lang="it" /><text lang="it">lop</text><text lang="en">linux</text></attribute><attribute name="Checkbox" attributetype="CheckBox" /></attributes><status>DRAFT</status></content>
', '20050503181212', '20061221161143', '<?xml version="1.0" encoding="UTF-8"?>
<content id="RAH1" typecode="RAH" typedescr="Tipo_Semplice"><descr>Articolo</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Un bel titolo</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[test test]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="email" attributetype="Monotext" /><attribute name="Numero" attributetype="Number" /><attribute name="Correlati" attributetype="Link" /><attribute name="Allegati" attributetype="Attach"><resource resourcetype="Attach" id="7" lang="it" /><text lang="it">lop</text><text lang="en">linux</text></attribute><attribute name="Checkbox" attributetype="CheckBox" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART1', 'ART', 'Articolo', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART1" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Articolo</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Il titolo</text><text lang="en">The title</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Pippo</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Paperino</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Pluto</monotext></attribute></list><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.spiderman.org</urldest></link><text lang="it">Spiderman</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">Image description</text></attribute><attribute name="Data" attributetype="Date"><date>20040310</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', '20050503181212', '20060622202051', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART1" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Articolo</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Il titolo</text><text lang="en">The title</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Pippo</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Paperino</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Pluto</monotext></attribute></list><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.spiderman.org</urldest></link><text lang="it">Spiderman</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">Image description</text></attribute><attribute name="Data" attributetype="Date"><date>20040310</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN103', 'EVN', 'Contenuto 1 Coach', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN103" typecode="EVN" typedescr="Evento"><descr>Contenuto 1 Coach</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 1 Coach</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Corpo Testo Contenuto 1 Coach]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19990415</date></attribute><attribute name="DataFine" attributetype="Date"><date>20000414</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20061221165150', '20061223125859', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN103" typecode="EVN" typedescr="Evento"><descr>Contenuto 1 Coach</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 1 Coach</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Corpo Testo Contenuto 1 Coach]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19990415</date></attribute><attribute name="DataFine" attributetype="Date"><date>20000414</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'coach', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('RAH101', 'RAH', 'Contenuto 1 Customers', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="RAH101" typecode="RAH" typedescr="Tipo_Semplice"><descr>Contenuto 1 Customers</descr><groups mainGroup="customers" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 1 Customers</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[CorpoTesto Contenuto 1 Customers]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="email" attributetype="Monotext" /><attribute name="Numero" attributetype="Number" /><attribute name="Correlati" attributetype="Link" /><attribute name="Allegati" attributetype="Attach" /><attribute name="Checkbox" attributetype="CheckBox" /></attributes><status>DRAFT</status></content>
', '20061221164536', '20061221165755', '<?xml version="1.0" encoding="UTF-8"?>
<content id="RAH101" typecode="RAH" typedescr="Tipo_Semplice"><descr>Contenuto 1 Customers</descr><groups mainGroup="customers" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 1 Customers</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[CorpoTesto Contenuto 1 Customers]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="email" attributetype="Monotext" /><attribute name="Numero" attributetype="Number" /><attribute name="Correlati" attributetype="Link" /><attribute name="Allegati" attributetype="Attach" /><attribute name="Checkbox" attributetype="CheckBox" /></attributes><status>DRAFT</status></content>
', 'customers', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART104', 'ART', 'Contenuto 2 Coach', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART104" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 2 Coach</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 2 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.japsportal.org</urldest></link><text lang="it">Home jAPS</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Corpo Testo Contenuto&nbsp;2 Coach]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20070104</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', '20061221165750', '20070103143539', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART104" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 2 Coach</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 2 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.japsportal.org</urldest></link><text lang="it">Home jAPS</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Corpo Testo Contenuto&nbsp;2 Coach]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20070104</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', 'coach', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN23', 'EVN', 'Collezione Ingrao', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN23" typecode="EVN" typedescr="Evento"><descr>Collezione Ingrao</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Collezione Ingri</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Nei rinnovati spazi della Galleria Comunale d''Arte sono ospitate le opere pittoriche e scultoree, quelle che dell''intero lascito di Giovanni Ingri rientrano nel periodo moderno e contemporaneo.]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20080213</date></attribute><attribute name="DataFine" attributetype="Date"><date>20080222</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209100541', '20080209100546', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN23" typecode="EVN" typedescr="Evento"><descr>Collezione Ingrao</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Collezione Ingri</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[Nei rinnovati spazi della Galleria Comunale d''Arte sono ospitate le opere pittoriche e scultoree, quelle che dell''intero lascito di Giovanni Ingri rientrano nel periodo moderno e contemporaneo.]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20080213</date></attribute><attribute name="DataFine" attributetype="Date"><date>20080222</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN24', 'EVN', 'Castello dei bambini', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN24" typecode="EVN" typedescr="Evento"><descr>Castello dei bambini</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Castello dei bambini</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Descrizion evento Castello dei bambini</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20090318</date></attribute><attribute name="DataFine" attributetype="Date"><date>20090326</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209100714', '20080209100719', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN24" typecode="EVN" typedescr="Evento"><descr>Castello dei bambini</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Castello dei bambini</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Descrizion evento Castello dei bambini</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20090318</date></attribute><attribute name="DataFine" attributetype="Date"><date>20090326</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN41', 'EVN', 'Mostra della ciliegia', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN41" typecode="EVN" typedescr="Evento"><descr>Mostra della ciliegia</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Sagra della ciliegia</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Sagra della ciliegia</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20080106</date></attribute><attribute name="DataFine" attributetype="Date"><date>20080124</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209102901', '20080209102903', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN41" typecode="EVN" typedescr="Evento"><descr>Mostra della ciliegia</descr><groups mainGroup="coach" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Sagra della ciliegia</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Sagra della ciliegia</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20080106</date></attribute><attribute name="DataFine" attributetype="Date"><date>20080124</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'coach', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN21', 'EVN', 'Sagra delle fragole', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN21" typecode="EVN" typedescr="Evento"><descr>Sagra delle fragole</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Mostra Delle Fragole</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Mostre delle fragole</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20060113</date></attribute><attribute name="DataFine" attributetype="Date"><date>20060304</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209123547', '20080209123637', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN21" typecode="EVN" typedescr="Evento"><descr>Sagra delle fragole</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Mostra Delle Fragole</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Mostre delle fragole</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20060113</date></attribute><attribute name="DataFine" attributetype="Date"><date>20060304</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN25', 'EVN', 'TEATRO DELLE MERAVIGLIE', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN25" typecode="EVN" typedescr="Evento"><descr>TEATRO DELLE MERAVIGLIE</descr><groups mainGroup="coach"><group name="free" /></groups><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">TEATRO DELLE MERAVIGLIE</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>TEATRO DELLE MERAVIGLIE  Laboratori Creativi</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20071212</date></attribute><attribute name="DataFine" attributetype="Date"><date>20071222</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20080209100902', '20080209100915', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN25" typecode="EVN" typedescr="Evento"><descr>TEATRO DELLE MERAVIGLIE</descr><groups mainGroup="coach"><group name="free" /></groups><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">TEATRO DELLE MERAVIGLIE</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>TEATRO DELLE MERAVIGLIE  Laboratori Creativi</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20071212</date></attribute><attribute name="DataFine" attributetype="Date"><date>20071222</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'coach', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN191', 'EVN', 'Evento 1', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN191" typecode="EVN" typedescr="Evento"><descr>Evento 1</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo A - Evento 1</text><text lang="en">Title C - Event 1</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto Evento 1</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19960417</date></attribute><attribute name="DataFine" attributetype="Date"><date>19960617</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', '20060418142200', '20061221161157', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN191" typecode="EVN" typedescr="Evento"><descr>Evento 1</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo A - Evento 1</text><text lang="en">Title C - Event 1</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto Evento 1</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>19960417</date></attribute><attribute name="DataFine" attributetype="Date"><date>19960617</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link" /></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN194', 'EVN', 'Evento 4', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN194" typecode="EVN" typedescr="Evento"><descr>Evento 4</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo D - Evento 4</text><text lang="en">Title A - Event 4</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto&nbsp;Evento 4</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20220219</date></attribute><attribute name="DataFine" attributetype="Date"><date>20220419</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link"><attribute name="LinkCorrelati" attributetype="Link"><link type="content"><contentdest>ART1</contentdest></link><text lang="it">Link 1</text></attribute><attribute name="LinkCorrelati" attributetype="Link"><link type="page"><pagedest>pagina_11</pagedest></link><text lang="it">Link 2</text></attribute></list></attributes><status>DRAFT</status></content>
', '20060418142507', '20061221161128', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN194" typecode="EVN" typedescr="Evento"><descr>Evento 4</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo D - Evento 4</text><text lang="en">Title A - Event 4</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto&nbsp;Evento 4</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20220219</date></attribute><attribute name="DataFine" attributetype="Date"><date>20220419</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link"><attribute name="LinkCorrelati" attributetype="Link"><link type="content"><contentdest>ART1</contentdest></link><text lang="it">Link 1</text></attribute><attribute name="LinkCorrelati" attributetype="Link"><link type="page"><pagedest>pagina_11</pagedest></link><text lang="it">Link 2</text></attribute></list></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART112', 'ART', 'Contenuto 4 Coach', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART112" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 4 Coach</descr><groups mainGroup="coach"><group name="customers" /><group name="helpdesk" /></groups><categories><category id="general_cat2" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 4 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Corpo Testo Contenuto 4 Coach</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20060213</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status><version>2.2</version><lastEditor>admin</lastEditor><created>20071216174627</created><lastModified>20120803224723</lastModified></content>
', '20071216174627', '20120803224723', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART112" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 4 Coach</descr><groups mainGroup="coach"><group name="customers" /><group name="helpdesk" /></groups><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 4 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Corpo Testo Contenuto 4 Coach</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20060213</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>PUBLIC</status><version>2.0</version><lastEditor>admin</lastEditor><created>20071216174627</created><lastModified>20120803222829</lastModified></content>
', 'coach', '2.2', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART180', 'ART', 'una descrizione', 'READY', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART180" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione</descr><groups mainGroup="free" /><categories><category id="cat1" /><category id="general_cat1" /></categories><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">Descrizione foto</text></attribute><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>READY</status></content>
', '20051012105757', '20061221161136', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART180" typecode="ART" typedescr="Articolo rassegna stampa"><descr>una descrizione</descr><groups mainGroup="free" /><categories><category id="cat1" /></categories><attributes><attribute name="Titolo" attributetype="Text" /><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext" /><attribute name="Foto" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">Descrizione foto</text></attribute><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>READY</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART102', 'ART', 'Contenuto 2 Customers', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART102" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 2 Customers</descr><groups mainGroup="customers" /><categories><category id="general_cat1" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 2 Customers</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="content"><contentdest>ART111</contentdest></link><text lang="it">Contenuto autorizzato Gruppo Coath</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Corpo Testo Contenuto&nbsp;2 Customers</p>]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', '20061221164629', '20071215174925', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART102" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 2 Customers</descr><groups mainGroup="customers" /><categories><category id="general_cat1" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 2 Customers</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="content"><contentdest>ART111</contentdest></link><text lang="it">Contenuto autorizzato Gruppo Coath</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Corpo Testo Contenuto&nbsp;2 Customers</p>]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>DRAFT</status></content>
', 'customers', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('EVN193', 'EVN', 'Evento 3', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN193" typecode="EVN" typedescr="Evento"><descr>Evento 3</descr><groups mainGroup="free" /><categories><category id="evento" /><category id="general_cat2" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo C - Evento 3</text><text lang="en">Title D - Evento 3</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto Evento 3</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20170412</date></attribute><attribute name="DataFine" attributetype="Date"><date>20170912</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link"><attribute name="LinkCorrelati" attributetype="Link"><link type="content"><contentdest>ART1</contentdest></link><text lang="it">Link 1</text></attribute><attribute name="LinkCorrelati" attributetype="Link"><link type="page"><pagedest>pagina_11</pagedest></link><text lang="it">Link 2</text></attribute></list></attributes><status>DRAFT</status></content>
', '20060418142409', '20061221161125', '<?xml version="1.0" encoding="UTF-8"?>
<content id="EVN193" typecode="EVN" typedescr="Evento"><descr>Evento 3</descr><groups mainGroup="free" /><categories><category id="evento" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo C - Evento 3</text><text lang="en">Title D - Evento 3</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>CorpoTesto Evento 3</p>]]></hypertext></attribute><attribute name="DataInizio" attributetype="Date"><date>20170412</date></attribute><attribute name="DataFine" attributetype="Date"><date>20170912</date></attribute><attribute name="Foto" attributetype="Image" /><list attributetype="Monolist" name="LinkCorrelati" nestedtype="Link"><attribute name="LinkCorrelati" attributetype="Link"><link type="content"><contentdest>ART1</contentdest></link><text lang="it">Link 1</text></attribute><attribute name="LinkCorrelati" attributetype="Link"><link type="page"><pagedest>pagina_11</pagedest></link><text lang="it">Link 2</text></attribute></list></attributes><status>DRAFT</status></content>
', 'free', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART111', 'ART', 'Contenuto 3 Coach', 'PUBLIC', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART111" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 3 Coach</descr><groups mainGroup="coach"><group name="customers" /><group name="helpdesk" /></groups><categories><category id="general_cat1" /><category id="general_cat2" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 3 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Corpo Testo Contenuto 3 Coach</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20061213</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>PUBLIC</status><version>4.0</version><lastEditor>admin</lastEditor><created>20071215174627</created><lastModified>20120803222621</lastModified></content>
', '20071215174627', '20120803222621', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART111" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto 3 Coach</descr><groups mainGroup="coach"><group name="customers" /><group name="helpdesk" /></groups><categories><category id="general_cat1" /><category id="general_cat2" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto 3 Coach</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext"><attribute name="Autori" attributetype="Monotext"><monotext>Walter</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Marco</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>Eugenio</monotext></attribute><attribute name="Autori" attributetype="Monotext"><monotext>William</monotext></attribute></list><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>Corpo Testo Contenuto 3 Coach</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20061213</date></attribute><attribute name="Numero" attributetype="Number" /></attributes><status>PUBLIC</status><version>4.0</version><lastEditor>admin</lastEditor><created>20071215174627</created><lastModified>20120803222621</lastModified></content>
', 'coach', '4.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART120', 'ART', 'Contenuto degli amministratori 1', 'PUBLIC', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART120" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 1</descr><groups mainGroup="administrators" /><categories><category id="general_cat2" /><category id="general_cat3" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori"</text><text lang="en">Title of Administrator''s Content</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.japsportal.org</urldest></link><text lang="it">Pagina Iniziale jAPSPortal</text><text lang="en">jAPSPortal HomePage</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo primo contenuto del gruppo degli Amministratori</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of first Administrators Group&#39;s Content</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20090328</date></attribute><attribute name="Numero" attributetype="Number"><number>7</number></attribute></attributes><status>PUBLIC</status><version>1.0</version><lastEditor>admin</lastEditor><created>20080721125725</created><lastModified>20120803222703</lastModified></content>
', '20080721125725', '20120803222703', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART120" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 1</descr><groups mainGroup="administrators" /><categories><category id="general_cat2" /><category id="general_cat3" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori"</text><text lang="en">Title of Administrator''s Content</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.japsportal.org</urldest></link><text lang="it">Pagina Iniziale jAPSPortal</text><text lang="en">jAPSPortal HomePage</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo primo contenuto del gruppo degli Amministratori</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of first Administrators Group&#39;s Content</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20090328</date></attribute><attribute name="Numero" attributetype="Number"><number>7</number></attribute></attributes><status>PUBLIC</status><version>1.0</version><lastEditor>admin</lastEditor><created>20080721125725</created><lastModified>20120803222703</lastModified></content>
', 'administrators', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART121', 'ART', 'Contenuto degli amministratori 2', 'DRAFT', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART121" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 2</descr><groups mainGroup="administrators"><group name="free" /></groups><categories><category id="general_cat3" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori" 2</text><text lang="en">Title of Administrator''s Content &lt;2&gt;</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.w3.org/</urldest></link><text lang="it">Pagina Iniziale W3C</text><text lang="en">World Wide Web Consortium - Web Standards</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo secondo contenuto del gruppo degli Amministratori</p>
<p>
	Questo contenuto appartiene al Gruppo degli Amministratori ma dichiarato visibile agli utenti del gruppo del gruppo free.</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of second Administrators Group&#39;s Content</p>
<p>
	This content belongs to the Administrators Group was declared visible to guest users.</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20090330</date></attribute><attribute name="Numero" attributetype="Number"><number>78</number></attribute></attributes><status>DRAFT</status><version>2.1</version><lastEditor>admin</lastEditor><created>20080721143834</created><lastModified>20120803222911</lastModified></content>
', '20080721143834', '20120803222911', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART121" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 2</descr><groups mainGroup="administrators"><group name="free" /></groups><categories /><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori" 2</text><text lang="en">Title of Administrator''s Content &lt;2&gt;</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link"><link type="external"><urldest>http://www.w3.org/</urldest></link><text lang="it">Pagina Iniziale W3C</text><text lang="en">World Wide Web Consortium - Web Standards</text></attribute><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo secondo contenuto del gruppo degli Amministratori</p>
<p>
	Questo contenuto appartiene al Gruppo degli Amministratori ma dichiarato visibile agli utenti del gruppo del gruppo free.</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of second Administrators Group&#39;s Content</p>
<p>
	This content belongs to the Administrators Group was declared visible to guest users.</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date"><date>20090330</date></attribute><attribute name="Numero" attributetype="Number"><number>78</number></attribute></attributes><status>PUBLIC</status><version>2.0</version><lastEditor>admin</lastEditor><created>20080721143834</created><lastModified>20120803222859</lastModified></content>
', 'administrators', '2.1', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ART122', 'ART', 'Contenuto degli amministratori 3', 'PUBLIC', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART122" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 3</descr><groups mainGroup="administrators"><group name="customers" /></groups><categories><category id="general_cat3" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori" 3</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo terzo contenuto del gruppo degli Amministratori</p>
<p>
	Questo contenuto appartiene al Gruppo degli Amministratori ma dichiarato visibile agli utenti del gruppo customers.</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of third Administrators Group&#39;s Content</p>
<p>
	This content belongs to the Administrators Group was declared visible to customers users.</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>PUBLIC</status><version>1.0</version><lastEditor>admin</lastEditor><created>20080721143945</created><lastModified>20120803222932</lastModified></content>
', '20080721143945', '20120803222932', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ART122" typecode="ART" typedescr="Articolo rassegna stampa"><descr>Contenuto degli amministratori 3</descr><groups mainGroup="administrators"><group name="customers" /></groups><categories><category id="general_cat3" /></categories><attributes><attribute name="Titolo" attributetype="Text"><text lang="it">Titolo Contenuto degli "Amministratori" 3</text></attribute><list attributetype="Monolist" name="Autori" nestedtype="Monotext" /><attribute name="VediAnche" attributetype="Link" /><attribute name="CorpoTesto" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>
	Testo terzo contenuto del gruppo degli Amministratori</p>
<p>
	Questo contenuto appartiene al Gruppo degli Amministratori ma dichiarato visibile agli utenti del gruppo customers.</p>
]]></hypertext><hypertext lang="en"><![CDATA[<p>
	Text of third Administrators Group&#39;s Content</p>
<p>
	This content belongs to the Administrators Group was declared visible to customers users.</p>
]]></hypertext></attribute><attribute name="Foto" attributetype="Image" /><attribute name="Data" attributetype="Date" /><attribute name="Numero" attributetype="Number" /></attributes><status>PUBLIC</status><version>1.0</version><lastEditor>admin</lastEditor><created>20080721143945</created><lastModified>20120803222932</lastModified></content>
', 'administrators', '1.0', 'admin');
INSERT INTO dataobjects (contentid, datatype, descr, status, workxml, created, lastmodified, onlinexml, maingroup, currentversion, lasteditor) VALUES ('ALL4', 'ALL', 'Description', 'PUBLIC', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ALL4" typecode="ALL" typedescr="Content type with all attribute types"><descr>Description</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Attach" attributetype="Attach"><resource resourcetype="Attach" id="7" lang="it" /><text lang="it">text Attach</text></attribute><attribute name="Boolean" attributetype="Boolean"><boolean>true</boolean></attribute><attribute name="CheckBox" attributetype="CheckBox" /><attribute name="Date" attributetype="Date"><date>20100321</date></attribute><attribute name="Date2" attributetype="Date"><date>20120321</date></attribute><attribute name="Enumerator" attributetype="Enumerator"><monotext>a</monotext></attribute><attribute name="EnumeratorMap" attributetype="EnumeratorMap"><key>02</key><value>Value 2</value></attribute><attribute name="Hypertext" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>text Hypertext</p>]]></hypertext></attribute><attribute name="Image" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">text image</text></attribute><attribute name="Link" attributetype="Link"><link type="external"><urldest>http://www.google.com</urldest></link><text lang="it">text Link</text></attribute><attribute name="Longtext" attributetype="Longtext"><text lang="it">text Longtext</text></attribute><attribute name="Monotext" attributetype="Monotext"><monotext>text Monotext</monotext></attribute><attribute name="Monotext2" attributetype="Monotext"><monotext>aaaa@entando.com</monotext></attribute><attribute name="Number" attributetype="Number"><number>25</number></attribute><attribute name="Number2" attributetype="Number"><number>85</number></attribute><attribute name="Text" attributetype="Text"><text lang="it">text Text</text></attribute><attribute name="Text2" attributetype="Text"><text lang="it">bbbb@entando.com</text></attribute><attribute name="ThreeState" attributetype="ThreeState"><boolean>false</boolean></attribute><composite name="Composite" attributetype="Composite"><attribute name="Attach" attributetype="Attach"><resource resourcetype="Attach" id="5" lang="it" /><text lang="it">text Attach of Composite</text></attribute><attribute name="Boolean" attributetype="Boolean"><boolean>true</boolean></attribute><attribute name="CheckBox" attributetype="CheckBox" /><attribute name="Date" attributetype="Date"><date>20100328</date></attribute><attribute name="Enumerator" attributetype="Enumerator" /><attribute name="Hypertext" attributetype="Hypertext"><hypertext lang="it"><![CDATA[text Hypertext of Composite]]></hypertext></attribute><attribute name="Image" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">text Image of Composite</text></attribute><attribute name="Link" attributetype="Link"><link type="external"><urldest>http://www.google.com</urldest></link><text lang="it">text Link of Composite</text></attribute><attribute name="Longtext" attributetype="Longtext"><text lang="it">text Longtext of Composite</text></attribute><attribute name="Monotext" attributetype="Monotext"><monotext>text Monotext of Composite</monotext></attribute><attribute name="Number" attributetype="Number"><number>89</number></attribute><attribute name="Text" attributetype="Text"><text lang="it">text Text of Composite</text></attribute><attribute name="ThreeState" attributetype="ThreeState"><boolean>true</boolean></attribute></composite><list name="ListBoolea" attributetype="List" nestedtype="Boolean"><listlang lang="it" /><listlang lang="it"><attribute name="ListBoolea" attributetype="Boolean"><boolean>false</boolean></attribute><attribute name="ListBoolea" attributetype="Boolean"><boolean>true</boolean></attribute></listlang></list><list name="ListCheck" attributetype="List" nestedtype="CheckBox"><listlang lang="it" /><listlang lang="it"><attribute name="ListCheck" attributetype="CheckBox" /><attribute name="ListCheck" attributetype="CheckBox"><boolean>true</boolean></attribute></listlang></list><list name="ListDate" attributetype="List" nestedtype="Date"><listlang lang="it" /><listlang lang="it"><attribute name="ListDate" attributetype="Date"><date>20150311</date></attribute><attribute name="ListDate" attributetype="Date"><date>20150326</date></attribute></listlang></list><list name="ListEnum" attributetype="List" nestedtype="Enumerator"><listlang lang="it" /><listlang lang="it"><attribute name="ListEnum" attributetype="Enumerator"><monotext>a</monotext></attribute><attribute name="ListEnum" attributetype="Enumerator"><monotext>b</monotext></attribute></listlang></list><list name="ListMonot" attributetype="List" nestedtype="Monotext"><listlang lang="it" /><listlang lang="it"><attribute name="ListMonot" attributetype="Monotext"><monotext>aaa</monotext></attribute><attribute name="ListMonot" attributetype="Monotext"><monotext>bbbb</monotext></attribute></listlang></list><list name="ListNumber" attributetype="List" nestedtype="Number"><listlang lang="it" /><listlang lang="it"><attribute name="ListNumber" attributetype="Number"><number>11</number></attribute><attribute name="ListNumber" attributetype="Number"><number>22</number></attribute></listlang></list><list name="List3Stat" attributetype="List" nestedtype="ThreeState"><listlang lang="it" /><listlang lang="it"><attribute name="List3Stat" attributetype="ThreeState" /><attribute name="List3Stat" attributetype="ThreeState"><boolean>false</boolean></attribute><attribute name="List3Stat" attributetype="ThreeState"><boolean>true</boolean></attribute></listlang></list><list name="MonoLAtta" attributetype="Monolist" nestedtype="Attach"><attribute name="MonoLAtta" attributetype="Attach"><resource resourcetype="Attach" id="7" lang="it" /><text lang="it">ccccc</text></attribute></list><list name="MonoLBool" attributetype="Monolist" nestedtype="Boolean"><attribute name="MonoLBool" attributetype="Boolean"><boolean>false</boolean></attribute><attribute name="MonoLBool" attributetype="Boolean"><boolean>true</boolean></attribute></list><list name="MonoLChec" attributetype="Monolist" nestedtype="CheckBox"><attribute name="MonoLChec" attributetype="CheckBox" /><attribute name="MonoLChec" attributetype="CheckBox"><boolean>true</boolean></attribute></list><list name="MonoLCom" attributetype="Monolist" nestedtype="Composite"><composite name="MonoLCom" attributetype="Composite"><attribute name="Attach" attributetype="Attach" /><attribute name="Date" attributetype="Date"><date>20100304</date></attribute><attribute name="Enumerator" attributetype="Enumerator"><monotext>a</monotext></attribute><attribute name="Hypertext" attributetype="Hypertext"><hypertext lang="it"><![CDATA[ddddddd]]></hypertext></attribute><attribute name="Image" attributetype="Image" /><attribute name="Link" attributetype="Link" /><attribute name="Longtext" attributetype="Longtext"><text lang="it">eeeeeeeeee</text></attribute><attribute name="Monotext" attributetype="Monotext"><monotext>ffffffffffff</monotext></attribute><attribute name="Number" attributetype="Number"><number>25</number></attribute><attribute name="Text" attributetype="Text"><text lang="it">ggggggggggggg</text></attribute></composite></list><list name="MonoLCom2" attributetype="Monolist" nestedtype="Composite"><composite name="MonoLCom2" attributetype="Composite"><attribute name="Boolean" attributetype="Boolean"><boolean>false</boolean></attribute><attribute name="CheckBox" attributetype="CheckBox" /><attribute name="ThreeState" attributetype="ThreeState" /></composite><composite name="MonoLCom2" attributetype="Composite"><attribute name="Boolean" attributetype="Boolean"><boolean>true</boolean></attribute><attribute name="CheckBox" attributetype="CheckBox"><boolean>true</boolean></attribute><attribute name="ThreeState" attributetype="ThreeState"><boolean>false</boolean></attribute></composite></list><list name="MonoLDate" attributetype="Monolist" nestedtype="Date"><attribute name="MonoLDate" attributetype="Date"><date>20150319</date></attribute><attribute name="MonoLDate" attributetype="Date"><date>20150402</date></attribute></list><list name="MonoLEnum" attributetype="Monolist" nestedtype="Enumerator"><attribute name="MonoLEnum" attributetype="Enumerator"><monotext>a</monotext></attribute><attribute name="MonoLEnum" attributetype="Enumerator"><monotext>b</monotext></attribute></list><list name="MonoLHyper" attributetype="Monolist" nestedtype="Hypertext"><attribute name="MonoLHyper" attributetype="Hypertext"><hypertext lang="it"><![CDATA[hhhhhhhhhh]]></hypertext></attribute><attribute name="MonoLHyper" attributetype="Hypertext"><hypertext lang="it"><![CDATA[iiiiiiiiiiii]]></hypertext></attribute></list><list name="MonoLImage" attributetype="Monolist" nestedtype="Image"><attribute name="MonoLImage" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">llllllllllllll</text></attribute></list><list name="MonoLLink" attributetype="Monolist" nestedtype="Link"><attribute name="MonoLLink" attributetype="Link"><link type="external"><urldest>http://www.google.com</urldest></link><text lang="it">mmmmmmmmm</text></attribute><attribute name="MonoLLink" attributetype="Link"><link type="external"><urldest>http://www.entando.com</urldest></link><text lang="it">nnnnnnnnnnnnnn</text></attribute></list><list name="MonoLLong" attributetype="Monolist" nestedtype="Longtext"><attribute name="MonoLLong" attributetype="Longtext"><text lang="it">ooooooooooo</text></attribute></list><list name="MonoLMonot" attributetype="Monolist" nestedtype="Monotext"><attribute name="MonoLMonot" attributetype="Monotext"><monotext>ppppppppppp</monotext></attribute><attribute name="MonoLMonot" attributetype="Monotext"><monotext>qqqqqqqqq</monotext></attribute></list><list name="MonoLNumb" attributetype="Monolist" nestedtype="Number"><attribute name="MonoLNumb" attributetype="Number"><number>1</number></attribute><attribute name="MonoLNumb" attributetype="Number"><number>2</number></attribute></list><list name="MonoLText" attributetype="Monolist" nestedtype="Text"><attribute name="MonoLText" attributetype="Text"><text lang="it">rrrrrrrrrrr</text></attribute><attribute name="MonoLText" attributetype="Text"><text lang="it">sssssssssssss</text></attribute></list><list name="MonoL3stat" attributetype="Monolist" nestedtype="ThreeState"><attribute name="MonoL3stat" attributetype="ThreeState" /><attribute name="MonoL3stat" attributetype="ThreeState"><boolean>false</boolean></attribute><attribute name="MonoL3stat" attributetype="ThreeState"><boolean>true</boolean></attribute></list><attribute name="EnumeratorMapBis" attributetype="EnumeratorMap"><key>01</key><value>Value 1 Bis</value></attribute><attribute name="MARKER" attributetype="Monotext"><monotext>MARKER</monotext></attribute></attributes><status>PUBLIC</status><version>6.0</version><lastEditor>admin</lastEditor><created>20150321165042</created><lastModified>20150321171007</lastModified></content>
', '20140321165042', '20140321171007', '<?xml version="1.0" encoding="UTF-8"?>
<content id="ALL4" typecode="ALL" typedescr="Content type with all attribute types"><descr>Description</descr><groups mainGroup="free" /><categories /><attributes><attribute name="Attach" attributetype="Attach"><resource resourcetype="Attach" id="7" lang="it" /><text lang="it">text Attach</text></attribute><attribute name="Boolean" attributetype="Boolean"><boolean>true</boolean></attribute><attribute name="CheckBox" attributetype="CheckBox" /><attribute name="Date" attributetype="Date"><date>20100321</date></attribute><attribute name="Date2" attributetype="Date"><date>20120321</date></attribute><attribute name="Enumerator" attributetype="Enumerator"><monotext>a</monotext></attribute><attribute name="EnumeratorMap" attributetype="EnumeratorMap"><key>02</key><value>Value 2 Old</value></attribute><attribute name="Hypertext" attributetype="Hypertext"><hypertext lang="it"><![CDATA[<p>text Hypertext</p>]]></hypertext></attribute><attribute name="Image" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">text image</text></attribute><attribute name="Link" attributetype="Link"><link type="external"><urldest>http://www.google.com</urldest></link><text lang="it">text Link</text></attribute><attribute name="Longtext" attributetype="Longtext"><text lang="it">text Longtext</text></attribute><attribute name="Monotext" attributetype="Monotext"><monotext>text Monotext</monotext></attribute><attribute name="Monotext2" attributetype="Monotext"><monotext>aaaa@entando.com</monotext></attribute><attribute name="Number" attributetype="Number"><number>25</number></attribute><attribute name="Number2" attributetype="Number"><number>85</number></attribute><attribute name="Text" attributetype="Text"><text lang="it">text Text</text></attribute><attribute name="Text2" attributetype="Text"><text lang="it">bbbb@entando.com</text></attribute><attribute name="ThreeState" attributetype="ThreeState"><boolean>false</boolean></attribute><composite name="Composite" attributetype="Composite"><attribute name="Attach" attributetype="Attach"><resource resourcetype="Attach" id="5" lang="it" /><text lang="it">text Attach of Composite</text></attribute><attribute name="Boolean" attributetype="Boolean"><boolean>true</boolean></attribute><attribute name="CheckBox" attributetype="CheckBox" /><attribute name="Date" attributetype="Date"><date>20100328</date></attribute><attribute name="Enumerator" attributetype="Enumerator" /><attribute name="Hypertext" attributetype="Hypertext"><hypertext lang="it"><![CDATA[text Hypertext of Composite]]></hypertext></attribute><attribute name="Image" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">text Image of Composite</text></attribute><attribute name="Link" attributetype="Link"><link type="external"><urldest>http://www.google.com</urldest></link><text lang="it">text Link of Composite</text></attribute><attribute name="Longtext" attributetype="Longtext"><text lang="it">text Longtext of Composite</text></attribute><attribute name="Monotext" attributetype="Monotext"><monotext>text Monotext of Composite</monotext></attribute><attribute name="Number" attributetype="Number"><number>89</number></attribute><attribute name="Text" attributetype="Text"><text lang="it">text Text of Composite</text></attribute><attribute name="ThreeState" attributetype="ThreeState"><boolean>true</boolean></attribute></composite><list name="ListBoolea" attributetype="List" nestedtype="Boolean"><listlang lang="it" /><listlang lang="it"><attribute name="ListBoolea" attributetype="Boolean"><boolean>false</boolean></attribute><attribute name="ListBoolea" attributetype="Boolean"><boolean>true</boolean></attribute></listlang></list><list name="ListCheck" attributetype="List" nestedtype="CheckBox"><listlang lang="it" /><listlang lang="it"><attribute name="ListCheck" attributetype="CheckBox" /><attribute name="ListCheck" attributetype="CheckBox"><boolean>true</boolean></attribute></listlang></list><list name="ListDate" attributetype="List" nestedtype="Date"><listlang lang="it" /><listlang lang="it"><attribute name="ListDate" attributetype="Date"><date>20150311</date></attribute><attribute name="ListDate" attributetype="Date"><date>20150326</date></attribute></listlang></list><list name="ListEnum" attributetype="List" nestedtype="Enumerator"><listlang lang="it" /><listlang lang="it"><attribute name="ListEnum" attributetype="Enumerator"><monotext>a</monotext></attribute><attribute name="ListEnum" attributetype="Enumerator"><monotext>b</monotext></attribute></listlang></list><list name="ListMonot" attributetype="List" nestedtype="Monotext"><listlang lang="it" /><listlang lang="it"><attribute name="ListMonot" attributetype="Monotext"><monotext>aaa</monotext></attribute><attribute name="ListMonot" attributetype="Monotext"><monotext>bbbb</monotext></attribute></listlang></list><list name="ListNumber" attributetype="List" nestedtype="Number"><listlang lang="it" /><listlang lang="it"><attribute name="ListNumber" attributetype="Number"><number>11</number></attribute><attribute name="ListNumber" attributetype="Number"><number>22</number></attribute></listlang></list><list name="List3Stat" attributetype="List" nestedtype="ThreeState"><listlang lang="it" /><listlang lang="it"><attribute name="List3Stat" attributetype="ThreeState" /><attribute name="List3Stat" attributetype="ThreeState"><boolean>false</boolean></attribute><attribute name="List3Stat" attributetype="ThreeState"><boolean>true</boolean></attribute></listlang></list><list name="MonoLAtta" attributetype="Monolist" nestedtype="Attach"><attribute name="MonoLAtta" attributetype="Attach"><resource resourcetype="Attach" id="7" lang="it" /><text lang="it">ccccc</text></attribute></list><list name="MonoLBool" attributetype="Monolist" nestedtype="Boolean"><attribute name="MonoLBool" attributetype="Boolean"><boolean>false</boolean></attribute><attribute name="MonoLBool" attributetype="Boolean"><boolean>true</boolean></attribute></list><list name="MonoLChec" attributetype="Monolist" nestedtype="CheckBox"><attribute name="MonoLChec" attributetype="CheckBox" /><attribute name="MonoLChec" attributetype="CheckBox"><boolean>true</boolean></attribute></list><list name="MonoLCom" attributetype="Monolist" nestedtype="Composite"><composite name="MonoLCom" attributetype="Composite"><attribute name="Attach" attributetype="Attach" /><attribute name="Date" attributetype="Date"><date>20100304</date></attribute><attribute name="Enumerator" attributetype="Enumerator"><monotext>a</monotext></attribute><attribute name="Hypertext" attributetype="Hypertext"><hypertext lang="it"><![CDATA[ddddddd]]></hypertext></attribute><attribute name="Image" attributetype="Image" /><attribute name="Link" attributetype="Link" /><attribute name="Longtext" attributetype="Longtext"><text lang="it">eeeeeeeeee</text></attribute><attribute name="Monotext" attributetype="Monotext"><monotext>ffffffffffff</monotext></attribute><attribute name="Number" attributetype="Number"><number>25</number></attribute><attribute name="Text" attributetype="Text"><text lang="it">ggggggggggggg</text></attribute></composite></list><list name="MonoLCom2" attributetype="Monolist" nestedtype="Composite"><composite name="MonoLCom2" attributetype="Composite"><attribute name="Boolean" attributetype="Boolean"><boolean>false</boolean></attribute><attribute name="CheckBox" attributetype="CheckBox" /><attribute name="ThreeState" attributetype="ThreeState" /></composite><composite name="MonoLCom2" attributetype="Composite"><attribute name="Boolean" attributetype="Boolean"><boolean>true</boolean></attribute><attribute name="CheckBox" attributetype="CheckBox"><boolean>true</boolean></attribute><attribute name="ThreeState" attributetype="ThreeState"><boolean>false</boolean></attribute></composite></list><list name="MonoLDate" attributetype="Monolist" nestedtype="Date"><attribute name="MonoLDate" attributetype="Date"><date>20150319</date></attribute><attribute name="MonoLDate" attributetype="Date"><date>20150402</date></attribute></list><list name="MonoLEnum" attributetype="Monolist" nestedtype="Enumerator"><attribute name="MonoLEnum" attributetype="Enumerator"><monotext>a</monotext></attribute><attribute name="MonoLEnum" attributetype="Enumerator"><monotext>b</monotext></attribute></list><list name="MonoLHyper" attributetype="Monolist" nestedtype="Hypertext"><attribute name="MonoLHyper" attributetype="Hypertext"><hypertext lang="it"><![CDATA[hhhhhhhhhh]]></hypertext></attribute><attribute name="MonoLHyper" attributetype="Hypertext"><hypertext lang="it"><![CDATA[iiiiiiiiiiii]]></hypertext></attribute></list><list name="MonoLImage" attributetype="Monolist" nestedtype="Image"><attribute name="MonoLImage" attributetype="Image"><resource resourcetype="Image" id="44" lang="it" /><text lang="it">llllllllllllll</text></attribute></list><list name="MonoLLink" attributetype="Monolist" nestedtype="Link"><attribute name="MonoLLink" attributetype="Link"><link type="external"><urldest>http://www.google.com</urldest></link><text lang="it">mmmmmmmmm</text></attribute><attribute name="MonoLLink" attributetype="Link"><link type="external"><urldest>http://www.entando.com</urldest></link><text lang="it">nnnnnnnnnnnnnn</text></attribute></list><list name="MonoLLong" attributetype="Monolist" nestedtype="Longtext"><attribute name="MonoLLong" attributetype="Longtext"><text lang="it">ooooooooooo</text></attribute></list><list name="MonoLMonot" attributetype="Monolist" nestedtype="Monotext"><attribute name="MonoLMonot" attributetype="Monotext"><monotext>ppppppppppp</monotext></attribute><attribute name="MonoLMonot" attributetype="Monotext"><monotext>qqqqqqqqq</monotext></attribute></list><list name="MonoLNumb" attributetype="Monolist" nestedtype="Number"><attribute name="MonoLNumb" attributetype="Number"><number>1</number></attribute><attribute name="MonoLNumb" attributetype="Number"><number>2</number></attribute></list><list name="MonoLText" attributetype="Monolist" nestedtype="Text"><attribute name="MonoLText" attributetype="Text"><text lang="it">rrrrrrrrrrr</text></attribute><attribute name="MonoLText" attributetype="Text"><text lang="it">sssssssssssss</text></attribute></list><list name="MonoL3stat" attributetype="Monolist" nestedtype="ThreeState"><attribute name="MonoL3stat" attributetype="ThreeState" /><attribute name="MonoL3stat" attributetype="ThreeState"><boolean>false</boolean></attribute><attribute name="MonoL3stat" attributetype="ThreeState"><boolean>true</boolean></attribute></list><attribute name="EnumeratorMapBis" attributetype="EnumeratorMap"><key>01</key><value>Value 1 Bis Old</value></attribute><attribute name="MARKER" attributetype="Monotext"><monotext>MARKER</monotext></attribute></attributes><status>PUBLIC</status><version>6.0</version><lastEditor>admin</lastEditor><created>20150321165042</created><lastModified>20150321171007</lastModified></content>
', 'free', '6.0', 'admin');




INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART122', 'general_cat3', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART122', 'general', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART122', NULL, 'administrators');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART122', NULL, 'customers');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('RAH101', NULL, 'customers');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('RAH1', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART104', NULL, 'coach');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART102', 'general', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART102', 'general_cat1', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART102', NULL, 'customers');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART180', 'cat1', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART180', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART1', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART187', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN25', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN25', NULL, 'coach');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN41', NULL, 'coach');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN103', NULL, 'coach');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN193', 'evento', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN193', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN194', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN191', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN21', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN24', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN23', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN192', 'evento', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN192', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('EVN20', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART111', 'general', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART111', 'general_cat1', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART111', 'general_cat2', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART111', NULL, 'coach');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART111', NULL, 'customers');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART111', NULL, 'helpdesk');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART120', 'general_cat3', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART120', 'general', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART120', 'general_cat2', NULL);
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART120', NULL, 'administrators');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART112', NULL, 'coach');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART112', NULL, 'customers');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART112', NULL, 'helpdesk');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART121', NULL, 'free');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ART121', NULL, 'administrators');
INSERT INTO datatyperelations (contentid, refcategory, refgroup) VALUES ('ALL4', NULL, 'free');




INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART112', 'Data', NULL, '2006-02-13 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART121', 'Data', NULL, '2009-03-30 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART121', 'Numero', NULL, NULL, 78, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART104', 'Data', NULL, '2007-01-04 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART1', 'Data', NULL, '2004-03-10 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'Titolo', 'TEATRO DELLE MERAVIGLIE', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'Titolo', 'TEATRO DELLE MERAVIGLIE', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'DataInizio', NULL, '2007-12-12 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'DataFine', NULL, '2007-12-22 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'Titolo', 'Sagra della ciliegia', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'Titolo', 'Sagra della ciliegia', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'DataInizio', NULL, '2008-01-06 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'DataFine', NULL, '2008-01-24 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'Titolo', 'Titolo Contenuto 1 Coach', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'Titolo', 'Titolo Contenuto 1 Coach', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'DataInizio', NULL, '1999-04-15 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'DataFine', NULL, '2000-04-14 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'Titolo', 'Titolo C - Evento 3', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'Titolo', 'Title D - Evento 3', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'DataInizio', NULL, '2017-04-12 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'DataFine', NULL, '2017-09-12 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'Titolo', 'Titolo D - Evento 4', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'Titolo', 'Title A - Event 4', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'DataInizio', NULL, '2022-02-19 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'DataFine', NULL, '2022-04-19 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'Titolo', 'Titolo A - Evento 1', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'Titolo', 'Title C - Event 1', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'DataInizio', NULL, '1996-04-17 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'DataFine', NULL, '1996-06-17 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'Titolo', 'Mostra Delle Fragole', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'Titolo', 'Mostra Delle Fragole', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'DataInizio', NULL, '2006-01-13 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'DataFine', NULL, '2006-03-04 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'Titolo', 'Castello dei bambini', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'Titolo', 'Castello dei bambini', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'DataInizio', NULL, '2009-03-18 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'DataFine', NULL, '2009-03-26 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'Titolo', 'Collezione Ingri', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'Titolo', 'Collezione Ingri', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'DataInizio', NULL, '2008-02-13 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'DataFine', NULL, '2008-02-22 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'Titolo', 'Titolo B - Evento 2', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'Titolo', 'Title B - Event 2', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'DataInizio', NULL, '1999-04-14 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'DataFine', NULL, '1999-06-14 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'Titolo', 'Mostra Zootecnica', NULL, NULL, 'it');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'Titolo', 'Mostra Zootecnica', NULL, NULL, 'en');
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'DataInizio', NULL, '2006-02-13 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'DataFine', NULL, '2006-02-20 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART111', 'Data', NULL, '2006-12-13 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART120', 'Data', NULL, '2009-03-28 00:00:00', NULL, NULL);
INSERT INTO datatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART120', 'Numero', NULL, NULL, 7, NULL);




INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ART122', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ART121', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ART120', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ART111', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ART102', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ART180', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ART112', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ART104', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ART1', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ART187', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN193', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN194', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN191', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN25', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN21', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN41', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN24', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN23', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN103', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN192', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN20', 'Titolo', 'jacms:title');
INSERT INTO datatypeattributeroles (contentid, attrname, rolename) VALUES ('ALL4', 'Text', 'jacms:title');




INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART102', 'general');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART102', 'general_cat1');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART180', 'cat1');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART180', 'general');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART180', 'general_cat1');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART179', 'general');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART179', 'general_cat1');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART179', 'general_cat2');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('EVN193', 'evento');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('EVN193', 'general');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('EVN193', 'general_cat2');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('EVN192', 'evento');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('EVN192', 'general');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('EVN192', 'general_cat1');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART111', 'general');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART111', 'general_cat1');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART111', 'general_cat2');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART120', 'general_cat3');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART120', 'general');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART120', 'general_cat2');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART121', 'general_cat3');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART121', 'general');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART122', 'general_cat3');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART122', 'general');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART112', 'general');
INSERT INTO workdatatyperelations (contentid, refcategory) VALUES ('ART112', 'general_cat2');




INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART111', 'Data', NULL, '2006-12-13 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART120', 'Data', NULL, '2009-03-28 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART120', 'Numero', NULL, NULL, 7, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART121', 'Data', NULL, '2009-03-30 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART121', 'Numero', NULL, NULL, 78, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART112', 'Data', NULL, '2006-02-13 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART104', 'Data', NULL, '2007-01-04 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART1', 'Data', NULL, '2004-03-10 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('ART179', 'Data', NULL, '2009-07-16 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'Titolo', 'TEATRO DELLE MERAVIGLIE', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'Titolo', 'TEATRO DELLE MERAVIGLIE', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'DataInizio', NULL, '2007-12-12 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN25', 'DataFine', NULL, '2007-12-22 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'Titolo', 'Sagra della ciliegia', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'Titolo', 'Sagra della ciliegia', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'DataInizio', NULL, '2008-01-06 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN41', 'DataFine', NULL, '2008-01-24 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'Titolo', 'Titolo Contenuto 1 Coach', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'Titolo', 'Titolo Contenuto 1 Coach', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'DataInizio', NULL, '1999-04-15 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN103', 'DataFine', NULL, '2000-04-14 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'Titolo', 'Titolo C - Evento 3', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'Titolo', 'Title D - Evento 3', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'DataInizio', NULL, '2017-04-12 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN193', 'DataFine', NULL, '2017-09-12 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'Titolo', 'Titolo D - Evento 4', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'Titolo', 'Title A - Event 4', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'DataInizio', NULL, '2022-02-19 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN194', 'DataFine', NULL, '2022-04-19 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'Titolo', 'Titolo A - Evento 1', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'Titolo', 'Title C - Event 1', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'DataInizio', NULL, '1996-04-17 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN191', 'DataFine', NULL, '1996-06-17 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'Titolo', 'Mostra Delle Fragole', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'Titolo', 'Mostra Delle Fragole', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'DataInizio', NULL, '2006-01-13 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN21', 'DataFine', NULL, '2006-03-04 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'Titolo', 'Castello dei bambini', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'Titolo', 'Castello dei bambini', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'DataInizio', NULL, '2009-03-18 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN24', 'DataFine', NULL, '2009-03-26 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'Titolo', 'Collezione Ingri', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'Titolo', 'Collezione Ingri', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'DataInizio', NULL, '2008-02-13 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN23', 'DataFine', NULL, '2008-02-22 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'Titolo', 'Titolo B - Evento 2', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'Titolo', 'Title B - Event 2', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'DataInizio', NULL, '1999-04-14 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN192', 'DataFine', NULL, '1999-06-14 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'Titolo', 'Mostra Zootecnica', NULL, NULL, 'it');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'Titolo', 'Mostra Zootecnica', NULL, NULL, 'en');
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'DataInizio', NULL, '2006-02-13 00:00:00', NULL, NULL);
INSERT INTO workdatatypesearch (contentid, attrname, textvalue, datevalue, numvalue, langcode) VALUES ('EVN20', 'DataFine', NULL, '2006-02-20 00:00:00', NULL, NULL);




INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART122', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART121', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART120', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART111', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART102', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART180', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART112', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART104', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART1', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART179', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ART187', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN193', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN194', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN191', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN25', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN21', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN41', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN24', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN23', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN103', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN192', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('EVN20', 'Titolo', 'jacms:title');
INSERT INTO workdatatypeattributeroles (contentid, attrname, rolename) VALUES ('ALL4', 'Text', 'jacms:title');

