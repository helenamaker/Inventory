package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/jogos")
public class JogosServlet extends HttpServlet {

    private static final int POR_PAGINA = 20;

    private static class Jogo {
        String nome;
        int appId;
        String genero;

        Jogo(String nome, int appId, String genero) {
            this.nome = nome;
            this.appId = appId;
            this.genero = genero;
        }
    }

    private static final List<Jogo> JOGOS = Arrays.asList(
        new Jogo("Counter-Strike 2", 730, "Ação"),
        new Jogo("Dota 2", 570, "Estratégia"),
        new Jogo("Team Fortress 2", 440, "Ação"),
        new Jogo("Grand Theft Auto V", 271590, "Ação"),
        new Jogo("Red Dead Redemption 2", 1174180, "Ação"),
        new Jogo("Cyberpunk 2077", 1091500, "RPG"),
        new Jogo("ELDEN RING", 1245620, "RPG"),
        new Jogo("The Witcher 3: Wild Hunt", 292030, "RPG"),
        new Jogo("Stardew Valley", 413150, "Simulação"),
        new Jogo("Terraria", 105600, "Aventura"),
        new Jogo("Portal", 400, "Puzzle"),
        new Jogo("Portal 2", 620, "Puzzle"),
        new Jogo("Hollow Knight", 367520, "Aventura"),
        new Jogo("Hades", 1145360, "RPG"),
        new Jogo("Baldur's Gate 3", 1086940, "RPG"),
        new Jogo("Tom Clancy's Rainbow Six Siege", 359550, "Ação"),
        new Jogo("PUBG: BATTLEGROUNDS", 578080, "Ação"),
        new Jogo("Rust", 252490, "Sobrevivência"),
        new Jogo("Palworld", 1623730, "Sobrevivência"),
        new Jogo("Raft", 648800, "Sobrevivência"),
        new Jogo("Valheim", 892970, "Sobrevivência"),
        new Jogo("Satisfactory", 526870, "Simulação"),
        new Jogo("Sons of the Forest", 1326470, "Sobrevivência"),
        new Jogo("Resident Evil 4", 2050650, "Terror"),
        new Jogo("Resident Evil 2", 883710, "Terror"),
        new Jogo("Resident Evil 3", 952060, "Terror"),
        new Jogo("Resident Evil Village", 1196590, "Terror"),
        new Jogo("Halo Infinite", 1240440, "Ação"),
        new Jogo("Halo: The Master Chief Collection", 976730, "Ação"),
        new Jogo("DOOM Eternal", 782330, "Ação"),
        new Jogo("DOOM", 379720, "Ação"),
        new Jogo("Metro Exodus", 412020, "Ação"),
        new Jogo("Left 4 Dead 2", 550, "Ação"),
        new Jogo("Dead by Daylight", 381210, "Terror"),
        new Jogo("Phasmophobia", 739630, "Terror"),
        new Jogo("Lethal Company", 1966720, "Terror"),
        new Jogo("The Forest", 242760, "Sobrevivência"),
        new Jogo("Subnautica", 264710, "Sobrevivência"),
        new Jogo("Subnautica: Below Zero", 848450, "Sobrevivência"),
        new Jogo("No Man's Sky", 275850, "Aventura"),
        new Jogo("The Elder Scrolls V: Skyrim Special Edition", 489830, "RPG"),
        new Jogo("Fallout 4", 377160, "RPG"),
        new Jogo("Fallout: New Vegas", 22380, "RPG"),
        new Jogo("The Elder Scrolls Online", 306130, "RPG"),
        new Jogo("Monster Hunter: World", 582010, "RPG"),
        new Jogo("Monster Hunter Rise", 1446780, "RPG"),
        new Jogo("Sekiro: Shadows Die Twice", 814380, "RPG"),
        new Jogo("Dark Souls III", 374320, "RPG"),
        new Jogo("Dark Souls Remastered", 570940, "RPG"),
        new Jogo("Dark Souls II: Scholar of the First Sin", 335300, "RPG"),
        new Jogo("Mortal Kombat 11", 976310, "Luta"),
        new Jogo("Street Fighter 6", 1364780, "Luta"),
        new Jogo("TEKKEN 8", 1778820, "Luta"),
        new Jogo("GUILTY GEAR -STRIVE-", 1384160, "Luta"),
        new Jogo("DRAGON BALL FighterZ", 678950, "Luta"),
        new Jogo("Forza Horizon 5", 1551360, "Corrida"),
        new Jogo("Forza Horizon 4", 1293830, "Corrida"),
        new Jogo("Euro Truck Simulator 2", 227300, "Simulação"),
        new Jogo("American Truck Simulator", 270880, "Simulação"),
        new Jogo("Need for Speed Heat", 1222680, "Corrida"),
        new Jogo("Assetto Corsa", 244210, "Corrida"),
        new Jogo("Trackmania", 2225070, "Corrida"),
        new Jogo("Fall Guys", 1097150, "Casual"),
        new Jogo("Rocket League", 252950, "Esporte"),
        new Jogo("EA SPORTS FC 24", 2195250, "Esporte"),
        new Jogo("The Sims 4", 1222670, "Simulação"),
        new Jogo("Cities: Skylines", 255710, "Simulação"),
        new Jogo("Cities: Skylines II", 949230, "Simulação"),
        new Jogo("Planet Zoo", 703080, "Simulação"),
        new Jogo("Planet Coaster", 493340, "Simulação"),
        new Jogo("RimWorld", 294100, "Simulação"),
        new Jogo("Factorio", 427520, "Estratégia"),
        new Jogo("Oxygen Not Included", 457140, "Simulação"),
        new Jogo("Don't Starve Together", 322330, "Sobrevivência"),
        new Jogo("ARK: Survival Evolved", 346110, "Sobrevivência"),
        new Jogo("DayZ", 221100, "Sobrevivência"),
        new Jogo("7 Days to Die", 251570, "Sobrevivência"),
        new Jogo("Project Zomboid", 108600, "Sobrevivência"),
        new Jogo("Killing Floor 2", 232090, "Ação"),
        new Jogo("PAYDAY 2", 218620, "Ação"),
        new Jogo("Warframe", 230410, "Ação"),
        new Jogo("Destiny 2", 1085660, "Ação"),
        new Jogo("Apex Legends", 1172470, "Ação"),
        new Jogo("War Thunder", 236390, "Ação"),
        new Jogo("Path of Exile", 238960, "RPG"),
        new Jogo("Path of Exile 2", 2694490, "RPG"),
        new Jogo("Diablo IV", 2344520, "RPG"),
        new Jogo("Lost Ark", 1599340, "RPG"),
        new Jogo("Black Desert", 582660, "RPG"),
        new Jogo("New World: Aeternum", 1063730, "RPG"),
        new Jogo("FINAL FANTASY XIV Online", 39210, "RPG"),
        new Jogo("FINAL FANTASY VII REMAKE INTERGRADE", 1462040, "RPG"),
        new Jogo("Persona 5 Royal", 1687950, "RPG"),
        new Jogo("Persona 4 Golden", 111300, "RPG"),
        new Jogo("Persona 3 Reload", 2161700, "RPG"),
        new Jogo("Yakuza: Like a Dragon", 1235140, "RPG"),
        new Jogo("NieR:Automata", 524220, "RPG"),
        new Jogo("Tales of Arise", 740130, "RPG"),
        new Jogo("Marvel's Spider-Man Remastered", 1817070, "Ação"),
        new Jogo("Marvel's Spider-Man 2", 2651280, "Ação"),
        new Jogo("God of War", 1593500, "Ação"),
        new Jogo("God of War Ragnarök", 2322010, "Ação"),
        new Jogo("Horizon Zero Dawn Complete Edition", 1151640, "Ação"),
        new Jogo("Horizon Forbidden West Complete Edition", 2420110, "Ação"),
        new Jogo("Days Gone", 1259420, "Ação"),
        new Jogo("Death Stranding", 1190460, "Aventura"),
        new Jogo("Ghost of Tsushima DIRECTOR'S CUT", 2215430, "Ação"),
        new Jogo("The Last of Us Part I", 1888930, "Ação"),
        new Jogo("Uncharted: Legacy of Thieves Collection", 1659420, "Ação"),
        new Jogo("Detroit: Become Human", 1222140, "Aventura"),
        new Jogo("Heavy Rain", 960910, "Aventura"),
        new Jogo("Beyond: Two Souls", 960990, "Aventura"),
        new Jogo("Stray", 1332010, "Aventura"),
        new Jogo("Little Nightmares", 424840, "Terror"),
        new Jogo("Little Nightmares II", 860510, "Terror"),
        new Jogo("INSIDE", 304430, "Aventura"),
        new Jogo("LIMBO", 48000, "Aventura"),
        new Jogo("Cuphead", 268910, "Ação"),
        new Jogo("Celeste", 504230, "Aventura"),
        new Jogo("Dead Cells", 588650, "Ação"),
        new Jogo("Ori and the Blind Forest", 261570, "Aventura"),
        new Jogo("Ori and the Will of the Wisps", 1057090, "Aventura"),
        new Jogo("BioShock Infinite", 8870, "Ação"),
        new Jogo("Borderlands 3", 397540, "Ação"),
        new Jogo("Control Ultimate Edition", 870780, "Ação"),
        new Jogo("The Evil Within 2", 601430, "Terror"),
        new Jogo("Outlast", 238320, "Terror"),
        new Jogo("Outlast 2", 414700, "Terror"),
        new Jogo("Amnesia: The Dark Descent", 57300, "Terror"),
        new Jogo("Amnesia: The Bunker", 1944430, "Terror"),
        new Jogo("SOMA", 282140, "Terror"),
        new Jogo("Alien: Isolation", 214490, "Terror"),
        new Jogo("Dying Light", 239140, "Ação"),
        new Jogo("Dying Light 2 Stay Human", 534380, "Ação"),
        new Jogo("Dishonored", 205100, "Ação"),
        new Jogo("Dishonored 2", 403640, "Ação"),
        new Jogo("Prey", 480490, "Ação"),
        new Jogo("Deathloop", 1252330, "Ação"),
        new Jogo("HITMAN World of Assassination", 1659040, "Ação"),
        new Jogo("Sniper Elite 5", 1029690, "Ação"),
        new Jogo("Metal Gear Solid V: The Phantom Pain", 287700, "Ação"),
        new Jogo("Grand Theft Auto IV: The Complete Edition", 12210, "Ação"),
        new Jogo("Bully: Scholarship Edition", 12200, "Ação"),
        new Jogo("L.A. Noire", 110800, "Aventura"),
        new Jogo("Max Payne 3", 204100, "Ação"),
        new Jogo("Sleeping Dogs: Definitive Edition", 307690, "Ação"),
        new Jogo("Batman: Arkham Knight", 208650, "Ação"),
        new Jogo("Batman: Arkham City", 200260, "Ação"),
        new Jogo("Middle-earth: Shadow of Mordor", 241930, "Ação"),
        new Jogo("Middle-earth: Shadow of War", 356190, "RPG"),
        new Jogo("Half-Life", 70, "Ação"),
        new Jogo("Half-Life 2", 220, "Ação"),
        new Jogo("Half-Life 2: Episode One", 380, "Ação"),
        new Jogo("Half-Life 2: Episode Two", 420, "Ação"),
        new Jogo("Counter-Strike", 10, "Tiro"),
        new Jogo("Counter-Strike: Source", 240, "Tiro"),
        new Jogo("Garry's Mod", 4000, "Casual"),
        new Jogo("Left 4 Dead", 500, "Terror"),
        new Jogo("Killing Floor", 1250, "Terror"),
        new Jogo("PAYDAY 3", 1272080, "Ação"),
        new Jogo("Portal Stories: Mel", 317400, "Puzzle"),
        new Jogo("Black Mesa", 362890, "Ação"),
        new Jogo("BioShock", 7670, "Ação"),
        new Jogo("BioShock 2 Remastered", 409720, "Ação"),
        new Jogo("BioShock Remastered", 409710, "Ação"),
        new Jogo("Borderlands", 8980, "Ação"),
        new Jogo("Borderlands 2", 49520, "Ação"),
        new Jogo("Borderlands: The Pre-Sequel", 261640, "Ação"),
        new Jogo("Tiny Tina's Wonderlands", 1286680, "RPG"),
        new Jogo("The Outer Worlds", 578650, "RPG"),
        new Jogo("The Outer Worlds 2", 1920490, "RPG"),
        new Jogo("Mass Effect Legendary Edition", 1328670, "RPG"),
        new Jogo("Mass Effect Andromeda", 123800, "RPG"),
        new Jogo("Dragon Age: Inquisition", 1222690, "RPG"),
        new Jogo("Dragon Age: Origins", 47810, "RPG"),
        new Jogo("Dragon Age II", 12370, "RPG"),
        new Jogo("Baldur's Gate 1 Enhanced Edition", 228280, "RPG"),
        new Jogo("Baldur's Gate 2 Enhanced Edition", 257350, "RPG"),
        new Jogo("Divinity: Original Sin", 230230, "RPG"),
        new Jogo("Divinity: Original Sin 2", 435150, "RPG"),
        new Jogo("Pillars of Eternity", 291650, "RPG"),
        new Jogo("Pillars of Eternity II: Deadfire", 560130, "RPG"),
        new Jogo("Wasteland 3", 719040, "RPG"),
        new Jogo("Disco Elysium", 632470, "RPG"),
        new Jogo("Undertale", 391540, "RPG"),
        new Jogo("Deltarune", 1671210, "RPG"),
        new Jogo("Omori", 1150690, "RPG"),
        new Jogo("Octopath Traveler", 921570, "RPG"),
        new Jogo("Octopath Traveler II", 1971650, "RPG"),
        new Jogo("Bravely Default II", 1446650, "RPG"),
        new Jogo("Dragon Quest XI S", 1295510, "RPG"),
        new Jogo("Tales of Berseria", 429660, "RPG"),
        new Jogo("Tales of Vesperia", 738540, "RPG"),
        new Jogo("Scarlet Nexus", 775500, "RPG"),
        new Jogo("Code Vein", 678960, "RPG"),
        new Jogo("Nioh: Complete Edition", 485510, "RPG"),
        new Jogo("Nioh 2 – The Complete Edition", 1325200, "RPG"),
        new Jogo("Wo Long: Fallen Dynasty", 1448440, "RPG"),
        new Jogo("Lies of P", 1627720, "RPG"),
        new Jogo("Lords of the Fallen", 1501750, "RPG"),
        new Jogo("Dragon's Dogma: Dark Arisen", 367500, "RPG"),
        new Jogo("Dragon's Dogma 2", 2054970, "RPG"),
        new Jogo("Kingdom Come: Deliverance", 379430, "RPG"),
        new Jogo("Kingdom Come: Deliverance II", 1771300, "RPG"),
        new Jogo("Cyberpunk 2077: Phantom Liberty", 1042550, "RPG"),
        new Jogo("The Elder Scrolls V: Skyrim", 72850, "RPG"),
        new Jogo("Fallout 3", 22300, "RPG"),
        new Jogo("Fallout 76", 1151340, "RPG"),
        new Jogo("Fallout 4 VR", 611660, "RPG"),
        new Jogo("Starfield", 1716740, "RPG"),
        new Jogo("The Elder Scrolls IV: Oblivion", 22330, "RPG"),
        new Jogo("The Elder Scrolls III: Morrowind", 22320, "RPG"),
        new Jogo("Divinity: Dragon Commander", 243950, "Estratégia"),
        new Jogo("Dragon Age: The Veilguard", 1845910, "RPG"),
        new Jogo("South of Midnight", 1934570, "Aventura"),
        new Jogo("Avowed", 2457220, "RPG"),
        new Jogo("Banishers: Ghosts of New Eden", 1493640, "Aventura"),
        new Jogo("Life is Strange", 319630, "Aventura"),
        new Jogo("Life is Strange 2", 532210, "Aventura"),
        new Jogo("Life is Strange: True Colors", 936790, "Aventura"),
        new Jogo("Life is Strange: Double Exposure", 1265920, "Aventura"),
        new Jogo("Telltale Batman", 536220, "Aventura"),
        new Jogo("The Wolf Among Us", 250320, "Aventura"),
        new Jogo("Tales from the Borderlands", 330830, "Aventura"),
        new Jogo("The Walking Dead", 207610, "Aventura"),
        new Jogo("The Walking Dead: Season Two", 261030, "Aventura"),
        new Jogo("The Walking Dead: The Final Season", 866800, "Aventura"),
        new Jogo("Until Dawn", 2172010, "Terror"),
        new Jogo("The Quarry", 1577120, "Terror"),
        new Jogo("Alan Wake", 108710, "Terror"),
        new Jogo("Alan Wake 2", 1088850, "Terror"),
        new Jogo("Alan Wake's American Nightmare", 202750, "Terror"),
        new Jogo("Quantum Break", 474960, "Ação"),
        new Jogo("Max Payne", 12140, "Ação"),
        new Jogo("Max Payne 2", 12150, "Ação"),
        new Jogo("Mafia: Definitive Edition", 1030840, "Ação"),
        new Jogo("Mafia II: Definitive Edition", 1030830, "Ação"),
        new Jogo("Mafia III: Definitive Edition", 360430, "Ação"),
        new Jogo("Watch Dogs", 243470, "Ação"),
        new Jogo("Watch Dogs 2", 447040, "Ação"),
        new Jogo("Watch Dogs: Legion", 2231380, "Ação"),
        new Jogo("Far Cry 3", 220240, "Ação"),
        new Jogo("Far Cry 4", 298110, "Ação"),
        new Jogo("Far Cry 5", 552520, "Ação"),
        new Jogo("Far Cry 6", 2369390, "Ação"),
        new Jogo("Far Cry Primal", 371660, "Ação"),
        new Jogo("Far Cry New Dawn", 939960, "Ação"),
        new Jogo("Assassin's Creed II", 33230, "Ação"),
        new Jogo("Assassin's Creed III Remastered", 911400, "Ação"),
        new Jogo("Assassin's Creed IV Black Flag", 242050, "Ação"),
        new Jogo("Assassin's Creed Origins", 582160, "Ação"),
        new Jogo("Assassin's Creed Valhalla", 2208920, "Ação"),
        new Jogo("Assassin's Creed Mirage", 3035570, "Ação"),
        new Jogo("Tom Clancy's The Division", 365590, "Ação"),
        new Jogo("Tom Clancy's The Division 2", 2221490, "Ação"),
        new Jogo("Ghost Recon Wildlands", 460930, "Ação"),
        new Jogo("Rainbow Six Vegas 2", 15120, "Tiro"),
        new Jogo("Splinter Cell Blacklist", 235600, "Ação"),
        new Jogo("Prince of Persia", 19980, "Ação"),
        new Jogo("Prince of Persia: The Forgotten Sands", 13600, "Ação"),
        new Jogo("Beyond Good & Evil", 15100, "Aventura"),
        new Jogo("Rayman Legends", 270550, "Plataforma"),
        new Jogo("Rayman Origins", 207490, "Plataforma"),
        new Jogo("Trials Fusion", 245490, "Corrida"),
        new Jogo("TrackMania Turbo", 375900, "Corrida"),
        new Jogo("The Crew 2", 646910, "Corrida"),
        new Jogo("The Crew Motorfest", 2698940, "Corrida"),
        new Jogo("F1 24", 2488620, "Corrida"),
        new Jogo("F1 23", 2108330, "Corrida"),
        new Jogo("F1 22", 1692250, "Corrida"),
        new Jogo("DiRT Rally", 310560, "Corrida"),
        new Jogo("DiRT Rally 2.0", 690790, "Corrida"),
        new Jogo("WRC 10", 1591490, "Corrida"),
        new Jogo("WRC Generations", 1953520, "Corrida"),
        new Jogo("CarX Drift Racing Online", 635260, "Corrida"),
        new Jogo("BeamNG.drive", 284160, "Corrida"),
        new Jogo("Wreckfest", 228380, "Corrida"),
        new Jogo("SnowRunner", 1465360, "Simulação"),
        new Jogo("MudRunner", 675010, "Simulação"),
        new Jogo("Farming Simulator 22", 1248130, "Simulação"),
        new Jogo("Farming Simulator 25", 2300320, "Simulação"),
        new Jogo("House Flipper", 613100, "Simulação"),
        new Jogo("House Flipper 2", 1190970, "Simulação"),
        new Jogo("PowerWash Simulator", 1290000, "Simulação"),
        new Jogo("PC Building Simulator", 621060, "Simulação"),
        new Jogo("PC Building Simulator 2", 1447430, "Simulação"),
        new Jogo("Car Mechanic Simulator 2021", 1190000, "Simulação"),
        new Jogo("Cooking Simulator", 641320, "Simulação"),
        new Jogo("Gas Station Simulator", 1161580, "Simulação"),
        new Jogo("Thief Simulator", 704850, "Simulação"),
        new Jogo("Zoo Tycoon: Ultimate Animal Collection", 613880, "Simulação"),
        new Jogo("Two Point Hospital", 535930, "Simulação"),
        new Jogo("Two Point Campus", 1649080, "Simulação"),
        new Jogo("Tropico 6", 492720, "Estratégia"),
        new Jogo("Tropico 5", 245620, "Estratégia"),
        new Jogo("Tropico 4", 57690, "Estratégia"),
        new Jogo("Civilization VI", 289070, "Estratégia"),
        new Jogo("Civilization V", 8930, "Estratégia"),
        new Jogo("Civilization IV", 3900, "Estratégia"),
        new Jogo("Age of Empires II: Definitive Edition", 813780, "Estratégia"),
        new Jogo("Age of Empires IV", 1466860, "Estratégia"),
        new Jogo("Age of Empires III: Definitive Edition", 933110, "Estratégia"),
        new Jogo("Age of Mythology: Retold", 1934680, "Estratégia"),
        new Jogo("Company of Heroes 2", 231430, "Estratégia"),
        new Jogo("Company of Heroes 3", 1677280, "Estratégia"),
        new Jogo("Total War: WARHAMMER III", 1142710, "Estratégia"),
        new Jogo("Total War: WARHAMMER II", 594570, "Estratégia"),
        new Jogo("Total War: THREE KINGDOMS", 779340, "Estratégia"),
        new Jogo("Total War: ROME II", 214950, "Estratégia"),
        new Jogo("XCOM: Enemy Unknown", 200510, "Estratégia"),
        new Jogo("XCOM 2", 268500, "Estratégia"),
        new Jogo("XCOM: Chimera Squad", 882100, "Estratégia"),
        new Jogo("Command & Conquer Remastered Collection", 1213210, "Estratégia"),
        new Jogo("They Are Billions", 644930, "Estratégia"),
        new Jogo("Frostpunk", 323190, "Estratégia"),
        new Jogo("Frostpunk 2", 1601580, "Estratégia"),
        new Jogo("Banished", 242920, "Estratégia"),
        new Jogo("Northgard", 466560, "Estratégia"),
        new Jogo("Endless Legend", 289130, "Estratégia"),
        new Jogo("Endless Space 2", 392110, "Estratégia"),
        new Jogo("Stellaris", 281990, "Estratégia"),
        new Jogo("Europa Universalis IV", 236850, "Estratégia"),
        new Jogo("Hearts of Iron IV", 394360, "Estratégia"),
        new Jogo("Crusader Kings III", 1158310, "Estratégia"),
        new Jogo("Victoria 3", 529340, "Estratégia"),
        new Jogo("Manor Lords", 1363080, "Estratégia"),
        new Jogo("Against the Storm", 1336490, "Estratégia"),
        new Jogo("Songs of Syx", 1162750, "Estratégia"),
        new Jogo("Into the Breach", 590380, "Estratégia"),
        new Jogo("Slay the Spire", 646570, "Estratégia"),
        new Jogo("Balatro", 2379780, "Estratégia"),
        new Jogo("Inscryption", 1092790, "Estratégia"),
        new Jogo("Monster Train", 1102190, "Estratégia"),
        new Jogo("Darkest Dungeon", 262060, "RPG"),
        new Jogo("Darkest Dungeon II", 1940340, "RPG"),
        new Jogo("Hades II", 1145350, "RPG"),
        new Jogo("Bastion", 107100, "RPG"),
        new Jogo("Transistor", 237930, "RPG"),
        new Jogo("Pyre", 462770, "RPG"),
        new Jogo("Hyper Light Drifter", 257850, "Ação"),
        new Jogo("Tunic", 553420, "Aventura"),
        new Jogo("Death's Door", 894020, "Aventura"),
        new Jogo("The Messenger", 764790, "Plataforma"),
        new Jogo("Shovel Knight: Treasure Trove", 250760, "Plataforma"),
        new Jogo("A Hat in Time", 253230, "Plataforma"),
        new Jogo("Psychonauts", 3830, "Aventura"),
        new Jogo("Psychonauts 2", 607080, "Aventura"),
        new Jogo("It Takes Two", 1426210, "Aventura"),
        new Jogo("A Way Out", 1222700, "Aventura"),
        new Jogo("Unravel", 1225560, "Aventura"),
        new Jogo("Unravel Two", 1225570, "Aventura"),
        new Jogo("Brothers: A Tale of Two Sons", 225080, "Aventura"),
        new Jogo("Journey", 638230, "Aventura"),
        new Jogo("ABZÛ", 384190, "Aventura"),
        new Jogo("What Remains of Edith Finch", 501300, "Aventura"),
        new Jogo("Firewatch", 383870, "Aventura"),
        new Jogo("Oxenfree", 388880, "Aventura"),
        new Jogo("Oxenfree II: Lost Signals", 1574020, "Aventura"),
        new Jogo("Night in the Woods", 481510, "Aventura"),
        new Jogo("Kentucky Route Zero", 231200, "Aventura"),
        new Jogo("The Stanley Parable", 221910, "Aventura"),
        new Jogo("The Stanley Parable: Ultra Deluxe", 1703340, "Aventura"),
        new Jogo("Papers, Please", 239030, "Simulação"),
        new Jogo("This War of Mine", 282070, "Estratégia"),
        new Jogo("Beholder", 475550, "Estratégia"),
        new Jogo("Beholder 2", 761620, "Estratégia"),
        new Jogo("FAR: Lone Sails", 609320, "Aventura"),
        new Jogo("FAR: Changing Tides", 1570010, "Aventura"),
        new Jogo("Inside My Radio", 318200, "Aventura"),
        new Jogo("Little Nightmares III", 1392860, "Terror"),
        new Jogo("Layers of Fear", 1946700, "Terror"),
        new Jogo("Layers of Fear 2", 1029890, "Terror"),
        new Jogo("Visage", 594330, "Terror"),
        new Jogo("MADiSON", 1670870, "Terror"),
        new Jogo("Devour", 1274570, "Terror"),
        new Jogo("The Mortuary Assistant", 1295920, "Terror"),
        new Jogo("Darkwood", 274520, "Terror"),
        new Jogo("Doki Doki Literature Club Plus!", 1388880, "Terror"),
        new Jogo("Five Nights at Freddy's", 319510, "Terror"),
        new Jogo("Five Nights at Freddy's 2", 332800, "Terror"),
        new Jogo("Five Nights at Freddy's 3", 354140, "Terror"),
        new Jogo("Five Nights at Freddy's 4", 388090, "Terror"),
        new Jogo("Five Nights at Freddy's: Security Breach", 747660, "Terror"),
        new Jogo("Bendy and the Ink Machine", 622650, "Terror"),
        new Jogo("Bendy and the Dark Revival", 1063660, "Terror"),
        new Jogo("Poppy Playtime", 1721470, "Terror"),
        new Jogo("Amnesia: Rebirth", 999220, "Terror"),
        new Jogo("Amnesia: A Machine for Pigs", 239200, "Terror"),
        new Jogo("Penumbra: Overture", 9870, "Terror"),
        new Jogo("Cry of Fear", 223710, "Terror"),
        new Jogo("Kholat", 343710, "Terror"),
        new Jogo("Observer", 514900, "Terror"),
        new Jogo("Layers of Fear (2016)", 391720, "Terror"),
        new Jogo("The Medium", 1293160, "Terror"),
        new Jogo("Scorn", 698670, "Terror"),
        new Jogo("SIGNALIS", 1262350, "Terror"),
        new Jogo("Tormented Souls", 1367590, "Terror"),
        new Jogo("Martha Is Dead", 515960, "Terror"),
        new Jogo("The Callisto Protocol", 1544020, "Terror"),
        new Jogo("Dead Space", 1693980, "Terror"),
        new Jogo("Dead Space 2", 47780, "Terror"),
        new Jogo("Dead Space 3", 1238060, "Terror"),
        new Jogo("Dishonored: Death of the Outsider", 614570, "Ação"),
        new Jogo("Wolfenstein: The New Order", 201810, "Ação"),
        new Jogo("Wolfenstein II: The New Colossus", 612880, "Ação"),
        new Jogo("Wolfenstein: Youngblood", 939550, "Ação"),
        new Jogo("DOOM 3", 208200, "Terror"),
        new Jogo("Quake", 2310, "Ação"),
        new Jogo("Quake II", 2320, "Ação"),
        new Jogo("Quake III Arena", 2200, "Ação"),
        new Jogo("RAGE", 9200, "Ação"),
        new Jogo("RAGE 2", 548570, "Ação"),
        new Jogo("Sniper Elite 4", 312660, "Ação"),
        new Jogo("Sniper Elite 3", 238090, "Ação"),
        new Jogo("Sniper Elite V2", 63380, "Ação"),
        new Jogo("Hitman 2", 863550, "Ação"),
        new Jogo("Shadow of the Tomb Raider", 750920, "Ação"),
        new Jogo("Rise of the Tomb Raider", 391220, "Ação"),
        new Jogo("Tomb Raider", 203160, "Ação"),
        new Jogo("Lara Croft and the Temple of Osiris", 289650, "Ação"),
        new Jogo("Just Cause 3", 225540, "Ação"),
        new Jogo("Just Cause 4", 517630, "Ação"),
        new Jogo("Sleeping Dogs", 202170, "Ação"),
        new Jogo("Prototype", 10150, "Ação"),
        new Jogo("Prototype 2", 115320, "Ação"),
        new Jogo("Saints Row IV", 206420, "Ação"),
        new Jogo("Saints Row: The Third Remastered", 978300, "Ação"),
        new Jogo("Saints Row", 742420, "Ação"),
        new Jogo("Lego Star Wars: The Skywalker Saga", 920210, "Aventura"),
        new Jogo("LEGO Marvel Super Heroes", 249130, "Aventura"),
        new Jogo("LEGO Batman 3: Beyond Gotham", 313690, "Aventura"),
        new Jogo("LEGO Harry Potter: Years 1-4", 21130, "Aventura"),
        new Jogo("Star Wars Jedi: Fallen Order", 1172380, "Ação"),
        new Jogo("Star Wars Jedi: Survivor", 1774580, "Ação"),
        new Jogo("Star Wars Battlefront II", 1237950, "Ação"),
        new Jogo("Star Wars: Squadrons", 1222730, "Ação"),
        new Jogo("Star Wars Knights of the Old Republic", 32370, "RPG"),
        new Jogo("Star Wars Knights of the Old Republic II", 208580, "RPG"),
        new Jogo("Star Wars: The Force Unleashed", 32430, "Ação"),
        new Jogo("Star Wars: Republic Commando", 6000, "Ação"),
        new Jogo("Mortal Kombat X", 307780, "Luta"),
        new Jogo("Mortal Kombat 1", 1971870, "Luta"),
        new Jogo("Street Fighter V", 310950, "Luta"),
        new Jogo("Street Fighter IV", 45760, "Luta"),
        new Jogo("TEKKEN 7", 389730, "Luta"),
        new Jogo("GUILTY GEAR XX ACCENT CORE PLUS R", 348550, "Luta"),
        new Jogo("Killer Instinct", 577940, "Luta"),
        new Jogo("Injustice: Gods Among Us Ultimate Edition", 242700, "Luta"),
        new Jogo("Injustice 2", 627270, "Luta"),
        new Jogo("Soulcalibur VI", 544750, "Luta"),
        new Jogo("Dragon Ball Xenoverse", 323470, "Luta"),
        new Jogo("Dragon Ball Xenoverse 2", 454640, "Luta"),
        new Jogo("Naruto Shippuden: Ultimate Ninja Storm 4", 349040, "Luta"),
        new Jogo("One Piece: Pirate Warriors 4", 1089090, "Ação"),
        new Jogo("Sonic Mania", 584400, "Plataforma"),
        new Jogo("Sonic Frontiers", 1237320, "Plataforma"),
        new Jogo("Sonic Generations", 71340, "Plataforma"),
        new Jogo("Sonic Adventure 2", 213610, "Plataforma"),
        new Jogo("Crash Bandicoot N. Sane Trilogy", 731490, "Plataforma"),
        new Jogo("Crash Bandicoot 4: It's About Time", 1378990, "Plataforma"),
        new Jogo("Spyro Reignited Trilogy", 996580, "Plataforma"),
        new Jogo("New Super Lucky's Tale", 847370, "Plataforma"),
        new Jogo("Yooka-Laylee", 360830, "Plataforma"),
        new Jogo("A Short Hike", 1055540, "Aventura"),
        new Jogo("Spiritfarer", 972660, "Aventura"),
        new Jogo("Gris", 683320, "Aventura"),
        new Jogo("Hollow Knight: Silksong", 1030300, "Aventura"),
        new Jogo("Blasphemous", 774361, "Ação"),
        new Jogo("Blasphemous 2", 2114740, "Ação"),
        new Jogo("Salt and Sanctuary", 283640, "Ação"),
        new Jogo("Salt and Sacrifice", 881100, "Ação"),
        new Jogo("Ender Lilies: Quietus of the Knights", 1369630, "RPG"),
        new Jogo("Deaths Gambit: Afterlife", 850170, "RPG"),
        new Jogo("Mortal Shell", 1110910, "RPG"),
        new Jogo("The Surge", 378540, "RPG"),
        new Jogo("The Surge 2", 644830, "RPG"),
        new Jogo("Remnant: From the Ashes", 617290, "Ação"),
        new Jogo("Remnant II", 1282100, "Ação"),
        new Jogo("Hellblade: Senua's Sacrifice", 414340, "Ação"),
        new Jogo("Hellblade II", 2461850, "Ação"),
        new Jogo("Plague Tale: Innocence", 752590, "Aventura"),
        new Jogo("A Plague Tale: Requiem", 1182900, "Aventura"),
        new Jogo("Metro 2033 Redux", 286690, "Ação"),
        new Jogo("Metro: Last Light Redux", 287390, "Ação"),
        new Jogo("Crysis", 17300, "Ação"),
        new Jogo("Crysis 2", 108800, "Ação"),
        new Jogo("Crysis 3", 24740, "Ação"),
        new Jogo("Titanfall 2", 1237970, "Ação"),
        new Jogo("Battlefield 1", 1238840, "Ação"),
        new Jogo("Battlefield V", 1238810, "Ação"),
        new Jogo("Battlefield 2042", 1517290, "Ação"),
        new Jogo("Battlefield 4", 1238860, "Ação"),
        new Jogo("Battlefield Hardline", 1238880, "Ação"),
        new Jogo("Rising Storm 2: Vietnam", 418460, "Tiro"),
        new Jogo("Insurgency", 222880, "Tiro"),
        new Jogo("Insurgency: Sandstorm", 581320, "Tiro"),
        new Jogo("Ready or Not", 1144200, "Tiro"),
        new Jogo("HELLDIVERS", 394510, "Ação"),
        new Jogo("HELLDIVERS 2", 553850, "Ação"),
        new Jogo("Deep Rock Galactic", 548430, "Ação")
    );

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String busca = request.getParameter("busca");
        String generoFiltro = request.getParameter("genero");
        String paginaTexto = request.getParameter("pagina");

        if (busca == null) {
            busca = "";
        }

        if (generoFiltro == null) {
            generoFiltro = "";
        }

        busca = busca.trim();
        generoFiltro = generoFiltro.trim();

        int pagina = 1;

        try {
            if (paginaTexto != null) {
                pagina = Integer.parseInt(paginaTexto);
            }

            if (pagina < 1) {
                pagina = 1;
            }

        } catch (Exception e) {
            pagina = 1;
        }

        List<Jogo> filtrados = new ArrayList<Jogo>();

        for (Jogo jogo : JOGOS) {

            boolean passaBusca =
                    busca.isEmpty()
                    || jogo.nome.toLowerCase(Locale.ROOT)
                    .contains(busca.toLowerCase(Locale.ROOT));

            boolean passaGenero =
                    generoFiltro.isEmpty()
                    || jogo.genero.equalsIgnoreCase(generoFiltro);

            if (passaBusca && passaGenero) {
                filtrados.add(jogo);
            }
        }

        int total = filtrados.size();

        int totalPaginas = Math.max(
                1,
                (int) Math.ceil(total / (double) POR_PAGINA)
        );

        if (pagina > totalPaginas) {
            pagina = totalPaginas;
        }

        int inicio = (pagina - 1) * POR_PAGINA;

        int fim = Math.min(
                inicio + POR_PAGINA,
                total
        );

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang='pt-BR'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Jogos - Inventory</title>");

        html.append("<link rel='preconnect' href='https://fonts.googleapis.com'>");
        html.append("<link rel='preconnect' href='https://fonts.gstatic.com' crossorigin>");
        html.append("<link href='https://fonts.googleapis.com/css2?family=Rajdhani:wght@400;500;600;700&display=swap' rel='stylesheet'>");

        html.append("<style>");

        html.append("*{box-sizing:border-box}");

        html.append(
            "body{margin:0;" +
            "background:linear-gradient(135deg,#0d0714,#160b24,#0d0714);" +
            "min-height:100vh;color:#fff;" +
            "font-family:'Rajdhani',sans-serif}"
        );

        html.append(
            "header{display:flex;align-items:center;" +
            "justify-content:space-between;gap:20px;" +
            "padding:20px 6%;background:#10091a;" +
            "border-bottom:1px solid #2e1a40;" +
            "position:sticky;top:0;z-index:10}"
        );

        html.append(
            "header h1{margin:0;color:#fff;font-size:27px;font-family:'Rajdhani',sans-serif;font-weight:700;letter-spacing:1px}"
        );

        html.append(
            "nav{display:flex;gap:20px;flex-wrap:wrap;" +
            "justify-content:center}"
        );

        html.append(
            "nav a{color:#ddd;text-decoration:none;font-size:14px}"
        );

        html.append(
            "nav a:hover{color:#c084fc}"
        );

        html.append(
            ".container{max-width:1250px;margin:0 auto;" +
            "padding:35px 20px 60px}"
        );

        html.append(
            ".titulo{text-align:center;color:#c084fc;" +
            "font-size:38px;margin:5px 0}"
        );

        html.append(
            ".filtros{display:flex;justify-content:center;" +
            "gap:10px;flex-wrap:wrap;margin-bottom:28px}"
        );

        html.append(
            ".filtros input,.filtros select{" +
            "background:#21152d;color:#fff;" +
            "border:1px solid #5b2a80;border-radius:9px;" +
            "padding:12px 14px;font-family:inherit;outline:none}"
        );

        html.append(
            ".filtros input{width:min(430px,90vw)}"
        );

        html.append(
            ".filtros button{border:0;border-radius:9px;" +
            "padding:12px 18px;" +
            "background:linear-gradient(135deg,#7c3aed,#9333ea);" +
            "color:white;font-weight:700;cursor:pointer}"
        );

        html.append(
            ".contador{text-align:center;color:#999;" +
            "margin:0 0 22px;font-size:14px}"
        );

        html.append(
            ".grid{display:grid;" +
            "grid-template-columns:repeat(5,minmax(0,1fr));" +
            "gap:22px}"
        );

        html.append(
            ".card{background:linear-gradient(145deg,#21152d,#17101f);" +
            "border:1px solid #38204d;padding:10px;" +
            "border-radius:15px;overflow:hidden;" +
            "transition:.25s;" +
            "box-shadow:0 8px 25px rgba(0,0,0,.3)}"
        );

        html.append(
            ".card:hover{transform:translateY(-6px);" +
            "border-color:#8b5cf6;" +
            "box-shadow:0 15px 35px rgba(124,58,237,.3)}"
        );

        html.append(
            ".capa{width:100%;aspect-ratio:2/3;" +
            "object-fit:cover;border-radius:10px;" +
            "display:block;background:#120d18}"
        );

        html.append(
            ".card h3{font-size:16px;line-height:1.3;" +
            "margin:13px 3px 8px;min-height:42px}"
        );

        html.append(
            ".tag{display:inline-block;background:#2d183e;" +
            "border:1px solid #4c2670;color:#c084fc;" +
            "border-radius:20px;padding:4px 8px;" +
            "font-size:11px;margin:2px}"
        );

        // =====================================================
        // BOTÕES DOS CARDS
        // =====================================================

        html.append(
            ".acoes{" +
            "display:flex;" +
            "flex-direction:column;" +
            "width:100%;" +
            "margin-top:10px;" +
            "gap:8px}"
        );

        html.append(
            ".acoes form{" +
            "margin:0;" +
            "padding:0;" +
            "width:100%}"
        );

        html.append(
            ".btn-biblioteca{" +
            "display:flex;" +
            "align-items:center;" +
            "justify-content:center;" +
            "width:100%;" +
            "min-height:40px;" +
            "padding:10px 12px;" +
            "background:linear-gradient(135deg,#7c3aed,#9333ea);" +
            "border:1px solid #8b5cf6;" +
            "border-radius:8px;" +
            "color:#fff;" +
            "text-decoration:none;" +
            "font-family:'Rajdhani',sans-serif;" +
            "font-size:12px;" +
            "font-weight:600;" +
            "text-align:center;" +
            "cursor:pointer;" +
            "transition:all .2s ease;" +
            "white-space:nowrap}"
        );

        html.append(
            ".btn-biblioteca:hover{" +
            "background:linear-gradient(135deg,#9333ea,#a855f7);" +
            "border-color:#c084fc;" +
            "transform:translateY(-1px);" +
            "box-shadow:0 5px 15px rgba(124,58,237,.35)}"
        );

        html.append(
            ".btn-favorito{" +
            "display:flex;" +
            "align-items:center;" +
            "justify-content:center;" +
            "width:100%;" +
            "min-height:40px;" +
            "padding:10px 12px;" +
            "background:#21152d;" +
            "border:1px solid #7c3aed;" +
            "border-radius:8px;" +
            "color:#c084fc;" +
            "font-family:'Rajdhani',sans-serif;" +
            "font-size:12px;" +
            "font-weight:600;" +
            "text-align:center;" +
            "cursor:pointer;" +
            "transition:all .2s ease;" +
            "white-space:nowrap}"
        );

        html.append(
            ".btn-favorito:hover{" +
            "background:#7c3aed;" +
            "border-color:#a855f7;" +
            "color:#fff;" +
            "transform:translateY(-1px);" +
            "box-shadow:0 5px 15px rgba(124,58,237,.3)}"
        );

        html.append(
            ".btn-trailer{display:flex;align-items:center;justify-content:center;" +
            "width:100%;min-height:40px;padding:10px 12px;" +
            "background:#120d18;border:1px solid #9333ea;border-radius:8px;" +
            "color:#e9d5ff;font-family:'Rajdhani',sans-serif;font-size:12px;" +
            "font-weight:600;text-align:center;cursor:pointer;transition:all .2s ease}"
        );

        html.append(
            ".btn-trailer:hover{background:#2d183e;border-color:#c084fc;color:#fff;" +
            "transform:translateY(-1px);box-shadow:0 5px 15px rgba(124,58,237,.3)}"
        );

        html.append(
            ".trailer-modal{display:none;position:fixed;inset:0;z-index:9999;" +
            "background:rgba(0,0,0,.82);align-items:center;justify-content:center;padding:20px;box-sizing:border-box}"
        );
        html.append(".trailer-modal.ativo{display:flex}");
        html.append(
            ".trailer-caixa{position:relative;width:min(1000px,95vw);background:#0d0914;" +
            "border:1px solid #6d28d9;border-radius:14px;padding:12px;box-shadow:0 20px 70px rgba(0,0,0,.7)}"
        );
        html.append(
            ".trailer-caixa iframe{display:block;width:100%;aspect-ratio:16/9;border:0;border-radius:9px;background:#000}"
        );
        html.append(
            ".fechar-trailer{position:absolute;right:18px;top:18px;z-index:2;width:38px;height:38px;" +
            "border:1px solid #9333ea;border-radius:50%;background:rgba(13,9,20,.92);" +
            "color:#fff;font-size:20px;cursor:pointer}"
        );

        html.append(
            ".paginacao{display:flex;justify-content:center;" +
            "gap:7px;flex-wrap:wrap;margin-top:32px}"
        );

        html.append(
            ".pagina{padding:9px 13px;border-radius:8px;" +
            "background:#21152d;border:1px solid #4c2670;" +
            "color:#ddd;text-decoration:none;font-size:14px}"
        );

        html.append(
            ".pagina:hover,.pagina.ativa{" +
            "background:#7c3aed;color:#fff;" +
            "border-color:#8b5cf6}"
        );

        html.append(
            ".vazio{grid-column:1/-1;text-align:center;" +
            "padding:50px;background:#17101f;" +
            "border:1px solid #38204d;border-radius:15px;" +
            "color:#aaa}"
        );

        html.append(
            "@media(max-width:1050px){" +
            ".grid{grid-template-columns:repeat(4,1fr)}}"
        );

        html.append(
            "@media(max-width:800px){" +
            "header{flex-direction:column}" +
            ".grid{grid-template-columns:repeat(3,1fr)}}"
        );

        html.append(
            "@media(max-width:600px){" +
            ".container{padding:22px 12px 45px}" +
            ".titulo{font-size:30px}" +
            ".grid{grid-template-columns:repeat(2,1fr);gap:13px}" +
            ".card{padding:8px}" +
            ".card h3{font-size:14px}" +
            ".btn-biblioteca,.btn-favorito{" +
            "font-size:10px;padding:9px 6px}" +
            "nav{gap:12px}}"
        );


        html.append("html,body{margin:0;padding:0;min-height:100%;}");
        html.append("body{font-family:'Rajdhani',sans-serif !important;background:radial-gradient(circle at top,#24143a 0%,#0b0910 45%) !important;color:#f4f4f5;min-height:100vh;}");
        html.append("header{width:100% !important;box-sizing:border-box;display:flex !important;align-items:center !important;justify-content:space-between !important;padding:18px 40px !important;background:#0d0914 !important;border-bottom:1px solid #30263a !important;position:relative !important;z-index:20 !important;backdrop-filter:none !important;}");
        html.append(".logo-area{display:flex !important;align-items:center !important;gap:9px !important;}");
        html.append(".logo-header{width:40px !important;height:40px !important;object-fit:contain;display:block;}");
        html.append(".logo-area h1{margin:0 !important;color:white !important;font-size:30px !important;font-weight:700 !important;font-family:'Rajdhani',sans-serif !important;}");
        html.append("header nav{display:flex !important;align-items:center !important;gap:28px !important;margin:0 !important;}");
        html.append("header nav a{color:#b9afc5 !important;text-decoration:none !important;font-size:14px !important;font-family:'Rajdhani',sans-serif !important;font-weight:400 !important;transition:.2s;}");
        html.append("header nav a:hover{color:#c084fc !important;}");
        html.append("@media(max-width:850px){header{padding:14px 20px !important;flex-wrap:wrap;gap:12px;}header nav{gap:15px !important;flex-wrap:wrap;}header nav a{font-size:13px !important;}}");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");

        html.append("<header>");

        html.append(
                "<div class='logo-area'>" +
                "<img src='icon.png' alt='Logo Inventory' class='logo-header'>" +
                "<h1>Inventory</h1>" +
                "</div>"
        );

        html.append("<nav>");
        html.append("<a href='index.html'>Início</a>");
        html.append("<a href='buscar-usuarios'>Buscar usuários</a>");
        html.append("<a href='jogos'>Jogos</a>");
        html.append("<a href='perfil'>Meu Perfil</a>");
        html.append("<a href='biblioteca'>Biblioteca</a>");
        html.append("<a href='" + request.getContextPath() + "/listas'>Listas</a>");
        html.append("<a href='logout'>Sair</a>");
        html.append("</nav>");

        html.append("</header>");

        html.append("<main class='container'>");

        html.append("<h2 class='titulo'>Explore os Jogos</h2>");

        html.append(
            "<form class='filtros' method='GET' action='jogos'>"
        );

        html.append(
            "<input type='search' name='busca' " +
            "placeholder='Pesquisar jogo...' value='"
        );

        html.append(escapar(busca));

        html.append("'>");

        html.append("<select name='genero'>");

        html.append(
            "<option value=''>Todos os gêneros</option>"
        );

        String[] generos = {
            "Ação",
            "Aventura",
            "RPG",
            "Terror",
            "Tiro",
            "Estratégia",
            "Corrida",
            "Esporte",
            "Simulação",
            "Plataforma",
            "Puzzle",
            "Sobrevivência",
            "Casual",
            "Luta"
        };

        for (String genero : generos) {

            html.append("<option value='");
            html.append(escapar(genero));
            html.append("'");

            if (genero.equalsIgnoreCase(generoFiltro)) {
                html.append(" selected");
            }

            html.append(">");
            html.append(escapar(genero));
            html.append("</option>");
        }

        html.append("</select>");
        html.append("<button type='submit'>Pesquisar</button>");
        html.append("</form>");

        html.append("<p class='contador'>Mostrando ");

        if (total == 0) {
            html.append("0");
        } else {
            html.append(inicio + 1);
            html.append("–");
            html.append(fim);
        }

        html.append(" de ");
        html.append(total);
        html.append(" jogos</p>");

        html.append("<section class='grid'>");

        if (total == 0) {

            html.append(
                "<div class='vazio'>Nenhum jogo encontrado.</div>"
            );

        } else {

            for (int i = inicio; i < fim; i++) {

                Jogo jogo = filtrados.get(i);

                String capa =
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/"
                    + jogo.appId
                    + "/library_600x900.jpg";

                String capaFallback =
                    "https://cdn.cloudflare.steamstatic.com/steam/apps/"
                    + jogo.appId
                    + "/header.jpg";

                html.append("<article class='card'>");

                html.append("<img class='capa' src='");
                html.append(capa);
                html.append("' alt='Capa de ");
                html.append(escapar(jogo.nome));
                html.append("' loading='lazy' ");

                html.append(
                    "onerror=\"this.onerror=null;" +
                    "this.src='"
                );

                html.append(capaFallback);
                html.append("';\">");

                html.append("<h3>");
                html.append(escapar(jogo.nome));
                html.append("</h3>");

                html.append("<span class='tag'>");
                html.append(escapar(jogo.genero));
                html.append("</span>");

                html.append("<div class='acoes'>");

                html.append(
                    "<a class='btn-biblioteca' " +
                    "href='adicionar-biblioteca?id="
                );

                html.append(jogo.appId);

                html.append("'>");
                html.append("+ Minha biblioteca");
                html.append("</a>");

                html.append(
                    "<form method='post' action='favorito'>"
                );

                html.append(
                    "<input type='hidden' " +
                    "name='steamAppId' value='"
                );

                html.append(jogo.appId);

                html.append("'>");

                html.append(
                    "<button type='submit' " +
                    "class='btn-favorito'>"
                );

                html.append("♡ Adicionar aos favoritos");

                html.append("</button>");

                html.append("</form>");

                html.append(
                    "<button type='button' class='btn-trailer' " +
                    "data-app-id='" + jogo.appId + "' " +
                    "data-nome='" + escapar(jogo.nome) + "' " +
                    "onclick='abrirTrailer(this)'>▶ Ver trailer</button>"
                );

                html.append("</div>");

                html.append("</article>");
            }
        }

        html.append("</section>");

        if (totalPaginas > 1) {

            html.append("<div class='paginacao'>");

            if (pagina > 1) {

                html.append(
                    linkPagina(
                        pagina - 1,
                        busca,
                        generoFiltro,
                        "‹ Anterior"
                    )
                );
            }

            for (int p = 1; p <= totalPaginas; p++) {

                if (
                    p == pagina
                    || p == 1
                    || p == totalPaginas
                    || Math.abs(p - pagina) <= 2
                ) {

                    html.append(
                        linkPagina(
                            p,
                            busca,
                            generoFiltro,
                            String.valueOf(p),
                            p == pagina
                        )
                    );
                }
            }

            if (pagina < totalPaginas) {

                html.append(
                    linkPagina(
                        pagina + 1,
                        busca,
                        generoFiltro,
                        "Próxima ›"
                    )
                );
            }

            html.append("</div>");
        }

        html.append("</main>");

        html.append(
            "<div id='trailerModal' class='trailer-modal' onclick='fecharTrailer()'>" +
            "<div class='trailer-caixa' onclick='event.stopPropagation()'>" +
            "<button type='button' class='fechar-trailer' onclick='fecharTrailer()'>×</button>" +
            "<iframe id='trailerFrame' title='Trailer do jogo' allow='autoplay; fullscreen' allowfullscreen></iframe>" +
            "</div></div>"
        );

        html.append(
            "<script>" +
            "function abrirTrailer(btn){var id=btn.getAttribute('data-app-id');var nome=encodeURIComponent(btn.getAttribute('data-nome')||'');document.getElementById('trailerFrame').src='trailer?appId='+id+'&nome='+nome;" +
            "document.getElementById('trailerModal').classList.add('ativo');document.body.style.overflow='hidden';}" +
            "function fecharTrailer(){document.getElementById('trailerFrame').src='';" +
            "document.getElementById('trailerModal').classList.remove('ativo');document.body.style.overflow='';}" +
            "document.addEventListener('keydown',function(e){if(e.key==='Escape')fecharTrailer();});" +
            "</script>"
        );

        html.append("</body>");
        html.append("</html>");

        response.getWriter().print(html.toString());
    }

    private static String linkPagina(
            int pagina,
            String busca,
            String genero,
            String texto) {

        return linkPagina(
            pagina,
            busca,
            genero,
            texto,
            false
        );
    }

    private static String linkPagina(
            int pagina,
            String busca,
            String genero,
            String texto,
            boolean ativa) {

        StringBuilder url =
            new StringBuilder("jogos?pagina=")
            .append(pagina);

        if (busca != null && !busca.isEmpty()) {
            url.append("&busca=");
            url.append(urlEncode(busca));
        }

        if (genero != null && !genero.isEmpty()) {
            url.append("&genero=");
            url.append(urlEncode(genero));
        }

        return "<a class='pagina"
                + (ativa ? " ativa" : "")
                + "' href='"
                + url.toString()
                + "'>"
                + escapar(texto)
                + "</a>";
    }

    private static String urlEncode(String texto) {

        try {
            return java.net.URLEncoder.encode(texto, "UTF-8");

        } catch (Exception e) {
            return texto;
        }
    }

    private static String escapar(String texto) {

        if (texto == null) {
            return "";
        }

        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}