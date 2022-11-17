# $Id: Makefile,v 1.1.2.1 2005/07/18 04:37:15 marcusa Exp $
# $Author: marcusa $
#
# Copyright (c) 2000 ERACOM Pty Ltd
# All Rights Reserved - Proprietary Information of ERACOM Pty. Ltd.
# Not to be Construed as a Published Work.
#
# $Source: /eracom/cvs/prod/csa8k/fw/fm/samples/smfs/fm/Attic/Makefile,v $
# $Revision: 1.1.2.1 $
# $Date: 2005/07/18 04:37:15 $

all: build

FM:=1

include ../cfgbuild.mak

NAME:=fmUX

LIBS:= \
	fmdebug  \
	fmcrt    \
	fmcprov  \
	fmcsa8k  \
	fmserial \
	fmsmfs

OBJS:=\
      Auhenticate          \
      bin2hex              \
      CalcCRC16            \
      CalcLRC              \
      CardCmd              \
      CheckFm              \
      CloseComPort         \
      DesWork              \
      EncDecData           \
      ExchangeKey          \
      fmUX                 \
      GetGBlock            \
      GetRandom            \
      GenSecretKey         \
      GetErrorString       \
      GISKEpck             \
      hdr                  \
      ident                \
      MICD                 \
      OpenComPort          \
      PrintComponent       \
      PrintReport          \
      Printer              \
      PPDetect             \
      PPZT588              \
      ReadCardSNum         \
      ReadPPInfo           \
      ReadSNum             \
      SendRecvPort         \
      SetPrinter           \
      smfs                 \
      TermComm             \
      U1_LoadKey           \
      U1_ReadKeyAttr       \
      U1_SendMasterKey     \
      U1_SendWorkKey       \
      U1_SetEntryMode      \
      U1_VerifyKey         \
      U3_InitWork          \
      U3_SendMasterKey     \
      U3_SendWorkKey       \
      U3_WriteEncryptedKey \
      UploadMKey           \
      UploadSAMkey         \
      VR_SendRecv          \
      USK                  \
      WriteSNum            \
      PassCode             \
      GetKeyBlock          \
      TagLite              \
      KbTools              \
      WrapUnwrapKey        \
      ZT_E1GF17

.phony: build
build: $(OBJDIR) $(ARCH_BIN)

ifeq ($(EMUL),)

$(ARCH_EXE): $(ARCH_OBJS) $(ARCH_LIBS)
	$(LN) $(LFLAGS) $(OUT)$@ $(ARCH_OBJS) $(ARCH_LIBS)

$(ARCH_BIN): $(ARCH_EXE)
	objcopy-fm -Obinary $< $@

else

$(ARCH_BIN): $(ARCH_LIBS) $(ARCH_OBJS)
	$(LN) $(LFLAGS) $(OUT)$(ARCH_DL) $(ARCH_OBJS) $(ARCH_LIBS)

endif
clean:
	-$(RM) $(ARCH_CLEAN)
	-$(RMDIR) $(subst /,$(PS),$(OBJDIR))

