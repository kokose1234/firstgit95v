/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C),2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation version, as published by
 the Free Software Foundation. You may not use, modify or distribute
 this program under any other version of the GNU Affero General Public
 License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public enum SendOpcodes implements WritableIntValueHolder {
    // CLogin::OnPacket
    CheckPasswordResult,
    AccountInfoResult,
    WorldInfomation,
    SelectWorldResult,
    SelectCharacterResult,
    CheckDuplicatedIDResult,
    CreateNewCharacterResult,
    DeleteCharacterResult,
    CheckGameGuardUpdatedResult,
    EnableSPWResult,
    CheckDeleteCharacterOTP,
    LatestConnectedWorld,
    RecommendWorldMessage,
    CheckSPWResult,
    CheckSSN2OnCreateNewCharacterResult,
    CheckSPWOnCreateNewCharacterResult,
    FirstSSNOnCreateNewCharacterResult,
    
    // CClientSocket::ProcessPacket
    MigrateCommand,
    AliveReq,
    AuthenCodeChanged,
    AuthenMessage,
    SecurityClient,
    GuardInspectProcess,
    CheckCrcResult,
    
    // CWvsContext::OnPacket
    InventoryOperation,
    InventoryGrow,
    StatChanged,
    TemporaryStatSet,
    TemporaryStatReset,
    ForcedStatSet,
    ForcedStatReset,
    ChangeSkillRecordResult,
    SkillUseResult,
    GivePopularityResult,
    Message,
    MemoResult,
    MapTransferResult,
    AntiMacroResult,
    ClaimResult,
    SetClaimSvrAvailableTime,
    ClaimSvrStatusChanged,
    SetTamingMobInfo,
    QuestClear,
    EntrustedShopCheckResult,
    SkillLearnItemResult,
    GatherItemResult,
    SortItemResult,
    CharacterInfo,
    PartyResult,
    ExpedtionResult,
    FriendResult,
    GuildResult,
    AllianceResult,
    TownPortal,
    BroadcastMsg,
    IncubatorResult,
    ShopScannerResult,
    ShopLinkResult,
    MarriageRequest,
    MarriageResult,
    WeddingGiftResult,
    NotifyMarriedPartnerMapTransfer,
    CashPetFoodResult,
    SetWeekEventMessage,
    SetPotionDiscountRate,
    BridleMobCatchFail,
    ImitatedNPCResult,
    ImitatedNPCData,
    LimitedNPCDisableInfo,
    MonsterBookSetCard,
    MonsterBookSetCover,
    HourChanged,
    MiniMapOnOff,
    ConsultAuthkeyUpdate,
    ClassCompetitionAuthkeyUpdate,
    WebBoardAuthkeyUpdate,
    SessionValue,
    PartyValue,
    FieldSetVariable,
    BonusExpRateChanged,
    FamilyChartResult,
    FamilyInfoResult,
    FamilyResult,
    FamilyJoinRequest,
    FamilyJoinRequestResult,
    FamilyJoinAccepted,
    FamilyPrivilegeList,
    FamilyFamousPointIncResult,
    FamilyNotifyLoginOrLogout,
    FamilySetPrivilege,
    FamilySummonRequest,
    NotifyLevelUp,
    NotifyWedding,
    NotifyJobChange,
    SetBuyEquipExt,
    ScriptProgressMessage,
    DataCRCCheckFailed,
    ShowSlotMessage,
    MacroSysDataInit,
    
    // CStage::OnPacket
    SetField,
    SetCashShop,
    
    // CMapLoadable::OnPacket
    SetBackEffect,
    SetMapObjectVisible,
    ClearBackEffect,
    
    // CField::OnPacket
    TransferFieldReqIgnored,
    TransferChannelReqIgnored,
    FieldSpecificData,
    GroupMessage,
    Whisper,
    SummonItemInavailable,
    FieldEffect,
    BlowWeather,
    PlayJukeBox,
    AdminResult,
    Quiz,
    Desc,
    Clock,
    SetQuestClear,
    SetQuestTime,
    SetObjectState,
    DestroyClock,
    StalkResult,
    QuickslotKeyMappedManInit,
    FootHoldInfo,
    RequestFootHoldInfo,
    ZakumTimer,
    
    // CField_ContiMove::OnPacket
    ContiMove,
    ContiState,
    
    // CField_Massacre::OnPacket
    MassacreIncGauge,
    
    // CField_MassacreResult::OnPacket
    MassacreResult,
    
    // CUserPool::OnPacket
    UserEnterField,
    UserLeaveField,
    
    // CUserPool::OnUserCommonPacket
    UserChat,
    UserADBoard,
    UserMiniRoomBalloon,
    UserSetConsumeItemEffect,
    UserShowItemUpgradeEffect,
    UserShowItemHyperUpgradeEffect,
    UserShowItemReleaseEffect,
    UserShowItemUnreleaseEffect,
    UserHitByUser,
    
    // CUser::OnPetPacket
    PetMove,
    PetAction,
    PetNameChanged,
    PetLoadExceptionList,
    PetActionCommand,
    
    // CUser::OnSummonedPacket
    SummonEnterField,
    SummonLeaveField,
    SummonMove,
    SummonAttack,
    SummonSkill,
    SummonHit,
    
    // CUser::OnDragonPacket
    DragonCreate,
    DragonMove,
    DragonDelete,
    
    // CUserPool::OnUserRemotePacket
    UserRemoteMove,
    UserRemoteMeleeAttack,
    UserRemoteShootAttack,
    UserRemoteMagicAttack,
    UserRemoteBodyAttack,
    UserRemoteSkillPrepare,
    UserRemoteSkillCancel,
    UserRemoteHit,
    UserRemoteEmotion,
    UserRemoteSetActiveEffectItem,
    UserRemoteShowUpgradeTombEffect,
    UserRemoteSitResult,
    UserRemoteAvatarModified,
    UserRemoteEffect,
    UserRemoteSetTemporaryStat,
    UserRemoteResetTemporaryStat,
    UserRemoteReceiveHP,
    UserRemoteGuildNameChanged,
    UserRemoteGuildMarkChanged,
    UserRemoteThrowGrenade,
    
    // CUserPool::OnUserLocalPacket
    UserLocalSitResult,
    UserLocalEmotion,
    UserLocalEffect,
    UserLocalTeleport,
    UserLocalMesoGive_Succeeded,
    UserLocalMesoGive_Failed,
    UserLocalQuestResult,
    UserLocalNotifyHPDecByField,
    UserLocalPetSkillChanged,
    UserLocalBalloonMsg,
    UserLocalPlayEventSound,
    UserLocalPlayMinigameSound,
    UserLocalMakerResult,
    UserLocalOpenConsultBoard,
    UserLocalOpenClassCompetitionPage,
    UserLocalOpenUI,
    UserLocalOpenUIWithOption,
    UserLocalSetDirectionMode,
    UserLocalSetStandAloneMode,
    UserLocalHireTutor,
    UserLocalTutorMsg,
    UserLocalIncComboResponse,
    UserLocalRadioSchedule,
    UserLocalOpenSkillGuide,
    UserLocalNoticeMsg,
    UserLocalChatMsg,
    UserLocalBuffzoneEffect,
    UserLocalDamageMeter,
    UserLocalTimeBombAttack,
    UserLocalSkillCooltimeSet,
    
    // CMobPool::OnPacket
    MobEnterField,
    MobLeaveField,
    MobChangeController,
    MobMove,
    MobCtrlAck,
    MobStatSet,
    MobStatReset,
    MobSuspendReset,
    MobAffected,
    MobDamaged,
    MobSpecialEffectBySkill,
    MobCrcKeyChanged,
    MobHPIndicator,
    MobCatchEffect,
    MobEffectByItem,
    MobSpeaking,
    MobSkillDelay,
    MobEscortFullPath,
    MobEscortStopEndPermmision,
    MobEscortStopSay,
    MobEscortReturnBefore,
    MobAttackedByMob,
    
    // CNpcPool::OnPacket
    NpcImitateData,
    UpdateLimitedDisableInfo,
    NpcEnterField,
    NpcLeaveField,
    NpcChangeController,
    NpcMove,
    NpcUpdateLimitedInfo,
    NpcSetSpecialAction,
    SetNpcScript,
    
    // CEmployeePool::OnPacket
    EmployeeEnterField,
    EmployeeLeaveField,
    EmployeeMiniRoomBalloon,
    
    // CDropPool::OnPacket
    DropEnterField,
    DropLeaveField,
    
    // CMessageBoxPool::OnPacket
    CreateFailed,
    MessageBoxEnterField,
    MessageBoxLeaveField,
    
    // CAffectedAreaPool::OnPacket
    AffectedAreaCreated,
    AffectedAreaRemoved,
    
    // CTownPortalPool::OnPacket
    TownPortalCreated,
    TownPortalRemoved,
    
    // CReactorPool::OnPacket
    ReactorChangeState,
    ReactorEnterField,
    ReactorLeaveField,
    
    // CField_SnowBall::OnPacket
    SnowBallState,
    SnowBallHit,
    SnowBallMsg,
    SnowBallTouch,
    
    // CField_Coconut::OnPacket
    CoconutHit,
    CoconutScore,
    
    // CField_GuildBoss::OnPacket
    HealerMove,
    PulleyStateChange,
    
    // CField_MonsterCarnival::OnPacket
    MonsterCarnivalEnter,
    MonsterCarnivalPersonalCP,
    MonsterCarnivalTeamCP,
    MonsterCarnivalRequestResult,
    MonsterCarnivalRequestResultE,
    MonsterCarnivalProcessForDeath,
    MonsterCarnivalShowMemberOutMsg,
    MonsterCarnivalShowGameResult,
    
    // CField_MonsterCarnivalRevive::OnPacket
    MonsterCarnivalReviveEnter,
    MonsterCarnivalReviveShowGameResult,
    
    // CField_AriantArena::OnPacket
    AriantArenaShowResult,
    AriantArenaUserScore,
    
    // CField_Battlefield::OnPacket
    BattlefieldScoreUpdate,
    BattlefieldTeamChanged,
    
    // CField_Witchtower::OnPacket
    WitchtowerScoreUpdate,
    
    // CScriptMan::OnPacket
    ScriptMessage,
    
    // CShopDlg::OnPacket
    ShopDlg,
    ShopTransAction,
    
    // CAdminShopDlg::OnPacket
    AdminShopRequest,
    AdminShopResult,
    
    // CTrunkDlg::OnPacket
    TrunkDlg,
    
    // CStoreBankDlg::OnPacket
    StoreBankMessage,
    StoreBankResult,
    
    // CRPSGameDlg::OnPacket
    RPSGameDlg,
    
    // CUIMessenger::OnPacket
    UIMessenger,
    
    // CMiniRoomBaseDlg::OnPacketBase
    MiniRoomBaseDlg,
    
    // CField_Tournament::OnPacket
    Tournament,
    TournamentMatchTable,
    TournamentSetPrize,
    TournamentUEW,
    TournamentAvatarInfo,
    
    // CField_Wedding::OnPacket
    WeddingProgress,
    WeddingCeremonyEnd,
    
    // CParcelDlg::OnPacket
    ParcelDlg,
    
    // CCashShop::OnPacket
    CashShopChargeParamResult,
    CashShopQueryCashResult,
    CashShopCashItemResult,
    CashShopPurchaseExpChanged,
    CashShopGiftMateInfoResult,
    CashShopMemberShopResult,
    
    // CFuncKeyMappedMan::OnPacket
    FuncKeyMappedManInit,
    PetConsumeItemInit,
    PetConsumeMPItemInit;

    private int code = -2;

    @Override
    public int getValue() {    
        return code;
    }

    @Override
    public void setValue(int value) {
        code = value;
    }

    static {
        reloadValues();
    }

    public static Properties getDefaultProperties() throws FileNotFoundException, IOException {
        Properties props = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("SendOpcode.properties")) {
            props.load(fileInputStream);
        }
        return props;
    }

    public static final void reloadValues() {
        try {
            ExternalCodeTableGetter.populateValues(getDefaultProperties(), values());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load sendops", e);
        }
    }
}
