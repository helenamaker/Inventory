# ============================================================
# baixar_capas_inventory_v3.ps1
# Baixa automaticamente capas para o projeto Inventory.
# ============================================================

$ErrorActionPreference = "Stop"

$pastaCapas = Join-Path (Get-Location) "src\main\webapp\imagensjogos\jogos"

New-Item -ItemType Directory -Force -Path $pastaCapas | Out-Null

$jogos = @(
    @{Nome="God of War"; Id="1593500"},
    @{Nome="Ghost of Tsushima"; Id="2215430"},
    @{Nome="Horizon Zero Dawn"; Id="1151640"},
    @{Nome="Horizon Forbidden West"; Id="2420110"},
    @{Nome="Marvel's Spider-Man Remastered"; Id="1817070"},
    @{Nome="Marvel's Spider-Man Miles Morales"; Id="1817190"},
    @{Nome="Uncharted Legacy of Thieves Collection"; Id="1659420"},
    @{Nome="Days Gone"; Id="1259420"},
    @{Nome="Dark Souls Remastered"; Id="570940"},
    @{Nome="Dark Souls II"; Id="236430"},
    @{Nome="Dark Souls III"; Id="374320"},
    @{Nome="Sekiro Shadows Die Twice"; Id="814380"},
    @{Nome="Lies of P"; Id="1627720"},
    @{Nome="Black Myth Wukong"; Id="2358720"},
    @{Nome="Dragon's Dogma 2"; Id="2054970"},
    @{Nome="Baldur's Gate 3"; Id="1086940"},
    @{Nome="Divinity Original Sin 2"; Id="435150"},
    @{Nome="Persona 5 Royal"; Id="1687950"},
    @{Nome="Persona 3 Reload"; Id="2161700"},
    @{Nome="Persona 4 Golden"; Id="1113000"},
    @{Nome="Final Fantasy VII Remake Intergrade"; Id="1462040"},
    @{Nome="Final Fantasy VII Rebirth"; Id="2909400"},
    @{Nome="Final Fantasy XVI"; Id="2515020"},
    @{Nome="Final Fantasy XV"; Id="637650"},
    @{Nome="Kingdom Hearts III"; Id="2552450"},
    @{Nome="NieR Automata"; Id="524220"},
    @{Nome="Monster Hunter World"; Id="582010"},
    @{Nome="Monster Hunter Rise"; Id="1446780"},
    @{Nome="Helldivers 2"; Id="553850"},
    @{Nome="The Last of Us Part II Remastered"; Id="2531310"},
    @{Nome="Detroit Become Human"; Id="1222140"},
    @{Nome="Heavy Rain"; Id="960910"},
    @{Nome="Until Dawn"; Id="2172010"},
    @{Nome="The Quarry"; Id="1577120"},
    @{Nome="Outlast"; Id="238320"},
    @{Nome="Outlast 2"; Id="414700"},
    @{Nome="Amnesia The Dark Descent"; Id="57300"},
    @{Nome="Alien Isolation"; Id="214490"},
    @{Nome="Dead Space"; Id="1693980"},
    @{Nome="Dead Space 2"; Id="47780"},
    @{Nome="Control"; Id="870780"},
    @{Nome="Death Stranding"; Id="1190460"},
    @{Nome="Red Dead Redemption"; Id="2668510"},
    @{Nome="Grand Theft Auto IV"; Id="12210"},
    @{Nome="Grand Theft Auto San Andreas"; Id="12120"},
    @{Nome="Grand Theft Auto Vice City"; Id="12110"},
    @{Nome="Grand Theft Auto III"; Id="12100"},
    @{Nome="Bully Scholarship Edition"; Id="12200"},
    @{Nome="Mafia Definitive Edition"; Id="1030840"},
    @{Nome="Mafia II"; Id="50130"},
    @{Nome="Assassin's Creed II"; Id="33230"},
    @{Nome="Assassin's Creed IV Black Flag"; Id="242050"},
    @{Nome="Assassin's Creed Origins"; Id="582160"},
    @{Nome="Assassin's Creed Odyssey"; Id="812140"},
    @{Nome="Assassin's Creed Valhalla"; Id="2208920"},
    @{Nome="Far Cry 3"; Id="220240"},
    @{Nome="Far Cry 4"; Id="298110"},
    @{Nome="Far Cry 5"; Id="552520"},
    @{Nome="Dying Light"; Id="239140"},
    @{Nome="Dying Light 2 Stay Human"; Id="534380"},
    @{Nome="Life is Strange"; Id="319630"},
    @{Nome="Life is Strange True Colors"; Id="936790"},
    @{Nome="Stray"; Id="1332010"},
    @{Nome="It Takes Two"; Id="1426210"},
    @{Nome="A Way Out"; Id="1222700"},
    @{Nome="Unravel Two"; Id="1225560"},
    @{Nome="Hogwarts Legacy"; Id="990080"},
    @{Nome="Star Wars Jedi Fallen Order"; Id="1172380"},
    @{Nome="Star Wars Jedi Survivor"; Id="1774580"},
    @{Nome="Marvel's Guardians of the Galaxy"; Id="1088850"},
    @{Nome="Batman Arkham Asylum GOTY"; Id="35140"},
    @{Nome="Batman Arkham City GOTY"; Id="200260"},
    @{Nome="Batman Arkham Knight"; Id="208650"},
    @{Nome="DOOM"; Id="379720"},
    @{Nome="DOOM Eternal"; Id="782330"},
    @{Nome="Mortal Kombat 11"; Id="976310"},
    @{Nome="Street Fighter 6"; Id="1364780"},
    @{Nome="TEKKEN 8"; Id="1778820"},
    @{Nome="EA SPORTS FC 24"; Id="2195250"},
    @{Nome="Rocket League"; Id="252950"},
    @{Nome="Apex Legends"; Id="1172470"},
    @{Nome="Call of Duty Modern Warfare II"; Id="1938090"},
    @{Nome="Call of Duty Black Ops"; Id="42700"},
    @{Nome="Battlefield 1"; Id="1238840"},
    @{Nome="Overwatch 2"; Id="2357670"},
    @{Nome="Counter-Strike 2"; Id="730"},
    @{Nome="Among Us"; Id="945360"},
    @{Nome="Terraria"; Id="105600"},
    @{Nome="Stardew Valley"; Id="413150"},
    @{Nome="The Sims 4"; Id="1222670"},
    @{Nome="Cities Skylines"; Id="255710"},
    @{Nome="Subnautica"; Id="264710"},
    @{Nome="No Man's Sky"; Id="275850"},
    @{Nome="Sea of Thieves"; Id="1172620"},
    @{Nome="Palworld"; Id="1623730"},
    @{Nome="Sonic Frontiers"; Id="1237320"},
    @{Nome="Cuphead"; Id="268910"},
    @{Nome="Hades"; Id="1145360"},
    @{Nome="Hollow Knight"; Id="367520"},
    @{Nome="Portal 2"; Id="620"},
    @{Nome="Half-Life 2"; Id="220"},
    @{Nome="Left 4 Dead 2"; Id="550"},
    @{Nome="Resident Evil 2"; Id="883710"},
    @{Nome="Resident Evil 3"; Id="952060"},
    @{Nome="Resident Evil 7"; Id="418370"},
    @{Nome="Resident Evil 5"; Id="21690"},
    @{Nome="Resident Evil 6"; Id="221040"},
    @{Nome="Metal Gear Solid V The Phantom Pain"; Id="287700"},
    @{Nome="Deathloop"; Id="1252330"},
    @{Nome="Dishonored"; Id="205100"},
    @{Nome="Prey"; Id="480490"},
    @{Nome="Mass Effect Legendary Edition"; Id="1328670"},
    @{Nome="Fallout 4"; Id="377160"},
    @{Nome="Skyrim Special Edition"; Id="489830"},
    @{Nome="Starfield"; Id="1716740"},
    @{Nome="Sea of Stars"; Id="1244090"},
    @{Nome="Yakuza 0"; Id="638970"},
    @{Nome="Yakuza Like a Dragon"; Id="1235140"},
    @{Nome="Like a Dragon Infinite Wealth"; Id="2072450"},
    @{Nome="Devil May Cry 5"; Id="601150"},
    @{Nome="Bayonetta"; Id="460790"},
    @{Nome="Little Nightmares"; Id="424840"}
)

$ok = 0
$falhas = New-Object System.Collections.Generic.List[string]

foreach ($jogo in $jogos) {

    $id = $jogo.Id
    $arquivo = Join-Path $pastaCapas "$id.jpg"

    $urls = @(
        "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/$id/library_600x900_2x.jpg",
        "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/$id/library_600x900.jpg",
        "https://shared.cloudflare.steamstatic.com/store_item_assets/steam/apps/$id/header.jpg"
    )

    Write-Host ""
    Write-Host "Baixando: $($jogo.Nome)" -ForegroundColor Cyan

    $baixou = $false

    foreach ($url in $urls) {

        try {

            & curl.exe `
                -k `
                -L `
                --fail `
                --silent `
                --show-error `
                --retry 2 `
                --connect-timeout 15 `
                --max-time 45 `
                -A "Mozilla/5.0" `
                -o "$arquivo" `
                "$url"

            if ($LASTEXITCODE -eq 0 -and
                (Test-Path $arquivo) -and
                ((Get-Item $arquivo).Length -gt 1000)) {

                Write-Host "OK" -ForegroundColor Green
                $ok++
                $baixou = $true
                break
            }

            Remove-Item "$arquivo" -Force -ErrorAction SilentlyContinue

        } catch {

            Remove-Item "$arquivo" -Force -ErrorAction SilentlyContinue
        }
    }

    if (-not $baixou) {

        Write-Host "FALHOU" -ForegroundColor Red
        [void]$falhas.Add($jogo.Nome)
    }
}

Write-Host ""
Write-Host "============================================" -ForegroundColor Yellow
Write-Host "CAPAS BAIXADAS: $ok" -ForegroundColor Green
Write-Host "CAPAS COM ERRO: $($falhas.Count)" -ForegroundColor Red
Write-Host "============================================" -ForegroundColor Yellow

if ($falhas.Count -gt 0) {

    Write-Host ""
    Write-Host "Jogos que falharam:" -ForegroundColor Red

    foreach ($nome in $falhas) {
        Write-Host "- $nome"
    }
}

Write-Host ""
Write-Host "Pasta das capas:" -ForegroundColor Cyan
Write-Host $pastaCapas
