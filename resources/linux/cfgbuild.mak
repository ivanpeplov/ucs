#############################################################################
#
#  This file is provided as part of the SafeNet Protect Toolkit FM SDK.
#
#  (c) Copyright 1997-2014 SafeNet, Inc. All rights reserved.
#  This file is protected by laws protecting trade secrets and confidential
#  information, as well as copyright laws and international treaties.
#
# Filename: cfgbuild.mak
# $Date: 2015/10/19 14:30:58GMT-05:00 $
#
#############################################################################
#
# Set up global build platform constants
# The constants defined are:
# - ARCH: The architecture for which the build is being performed
# - OSTYPE: (Internal) The architecture under which the build is being performed
# - PS: Path separator character
# - CC: The C compiler
# - LN: Linker
# - AR: Librarian
# - OUT: Flag to specify the output of the linker
# - COUT: Flag to specify the output from the compiler
# - Optional/additional flags: These constants must be added to the CFLAGS
#   definiton in individual makefiles to modify the default behavior.
#   - MISALIGN: C flag to define the behavior when data structures are not aligned.
#   - SHARED: Linker flag to build shared libraries.
#   - STATIC: Linker flag to prevent linking with dynamic libraries.
#   - THREADS_CFLAG: C flag for multi-threading support.
#   - THREADS_LFLAG: Linker flag for multi-threading support.
#   - SOCKET: Linker flag for sockets library support.
#
# The following dynamic constants adjust the names of user-defined types:
# - ARCH_EXE: Modifies the constant $(NAME) to be an executable name under the
#        $(OBJDIR)
#   ARCH_BIN: Modifies the constant $(NAME) to be an FM object name under the
#        $(OBJDIR)
# - ARCH_IMP: Modifies $(ARCH_BIN) to be a linkable library name for use in emulation
#        builds.(-lfmname in Linux, the import lib in Windows) 
# - ARCH_OBJS: Modifies the object list, $(OBJS). It prefixes them with the object
#        directory, and postfixes them with the proper object extension.
# - ARCH_LIBS: Modifies the library list, $(LIBS). It postfixes entries with the
#        proper library extension.
#
# The following constants are expected to be set
# - NAME: then name of the FM or application
#
# The following constants modify the behavior of the environment:
# - DEBUG: If defined, debugging is enabled 
#          in emulation mode this enables FM debug symbols.
# - CONSOLE: If defined, application is linked as a console application under
#   windows build. Otherwise, it is linked as a WINDOWS program.
# - DLL: If defined, DLL switches will be specified in the link flags
# - FM: If defined, the FM is being built (real or emulation)
# - EMUL: If defined, an build assumes an emulation of the fm
#
# The following constants are optional and can override the default
#   GCCFMDIR: If defined overrides default location compiler for FM build
#
#By default, go to host mode.
MODE=HOST
ifneq ($(EMUL),)
    $(info Building Emulation FM)
    MODE=EMUL
else ifneq ($(FM),)
    $(info Building Non-emulation FM)
    MODE=FM
else
    $(info Host mode)
    MODE=HOST
endif


DEFAULT_PPC_GCCDIR=/opt/safenet/fm-toolchain/fmgcc-ppc440e-1.0.0
FMLIBDIR?=$(FMDIR)/lib/$(ARCH)
CPROVLIBDIR?=$(CPROVDIR)/lib
ifeq ($(OS),Windows_NT)
CPROV_OSTYPE=win32
ifeq ($(DEBUG),1)
	LIBDIR=$(CPROVDIR)/libdbg/$(ARCH)
else
	LIBDIR=$(CPROVDIR)/lib/$(ARCH)	
endif
endif

.default: all

CFLAGS+=-I"$(CPROVDIR)/include" -I"$(CPROVDIR)/src/include" -I"$(FMDIR)/include"
ifneq ($(FM_NO_SETPRIVILEGELEVEL),$(NULL))
     CFLAGS+= -DFM_NO_SETPRIVILEGELEVEL
endif

NULL:=

# setup the name of the DLL to build or link against
# we expect 'NAME' to be defined
ifeq ($(MODE),EMUL)
DLNAME=fm-$(NAME)
    ifneq ($(FM),$(NULL))
       DLL:=1
    else
       ifneq ($(FM_NAME),$(NULL))
          DLNAME=fm-$(FM_NAME)
       endif
   endif
else
   DLNAME=$(NAME)
endif



############################################################################
#         Platform running this script is Win32                            #
############################################################################
ifeq ($(OS),Windows_NT)

ifneq ($(MODE), FM)
$(error On the Windows platform, this makefile can only be used to build FM modules. For Emulation, please use nmake)
endif
#DEFAULT_PPC_GCCDIR points to the folder where fmconfig.mk is located
override DEFAULT_PPC_GCCDIR=$(FMDIR)
endif  # end Building on Windows (FM)
############################################################################
#                         Unix Variants - Common                           #
############################################################################

# Path separator character
PS:=/
# C compiler
# CC - use default/override below
# Librarian/Archiver - may override below
AR:=ar -r
# Linker
LN:=$(LD)
# create a directory
MKDIR:=mkdir -p
# delete a directory
RMDIR:=rm -rf
# delete a file
RM:=rm -f
# copy a file
CP:=cp
# objcopy
OBJCOPY:=objcopy-fm

# File extensions of generated files
EXT_OBJ:=.o
EXT_LIB:=.a
EXT_EXE:=
EXT_DLL:=.so
PRE_DLL:=lib

# Default values. The detected platforms may overwrite these definitions
override ARCH:=unknown
ARCH_EXE=$(OBJDIR)/$(NAME)

# normally target 'NAME' is an application but if DLL is defined then it is a shared lib
ifneq ($(DLL),$(NULL))
ARCH_DL=$(OBJDIR)/lib$(NAME).so
endif

ARCH_OBJS=$(OBJS:%=$(OBJDIR)/%.o)
ARCH_LIBS=$(LIBS:%=-l%) $(FMLIBS:%=-l%)

ARCH_CLEAN=\
 $(ARCH_OBJS)\
 $(ARCH_EXE)\
 $(ARCH_DL)\
 $(ARCH_IMP)

OUT:=-o
COUT:=-o

LFLAGS += -L$(CPROVLIBDIR) -L$(FMLIBDIR)

# Is this the Adapter build for an FM and not an Emulation?

ifeq ($(MODE),FM)
ifeq ($(GCCFMDIR),)
    # default compiler location for PCIe2 FMs
    export GCCFMDIR:=$(DEFAULT_PPC_GCCDIR)
endif

#$ARCH override and toolchain config come from toolchain fmconfig.mk
ifneq ("$(wildcard $(GCCFMDIR)/fmconfig.mk)","")
    include $(GCCFMDIR)/fmconfig.mk
else
    $(error no $(GCCFMDIR)/fmconfig.mk)
endif

LN:=$(LD)
AR += -crs

# ensure fmcsa8k.a:_fm_init_ is linked in for all non-emul FM builds
LFLAGS += -u _fm_init_

# The ppc fmconfig.mk resets CFLAGS and LFLAGS... 
ARCH_OBJS=$(OBJS:%=$(OBJDIR)/%.o)
ARCH_EXE=$(OBJDIR)/$(NAME).elf
ARCH_BIN=$(OBJDIR)/$(NAME).bin

FMLIBS= \
	fmdebug \
	fmsmfs \
	fmcrt \
	fmcprov \
	fmcsa8k \
	fmserial \
	fmciphobj

ARCH_LIBS= --start-group $(LIBS:%=-l%) $(FMLIBS:%=-l%) -lgcc --end-group

LFLAGS += -L$(FMLIBDIR)

ARCH_CLEAN= $(ARCH_OBJS) $(ARCH_EXE) $(ARCH_BIN)

###############################################
# extra config for emulation build

else ifeq ($(MODE),EMUL)

CONTINUE=1

ifneq ($(FM),)
# EMULATION FM to be built as a lib
# default the name of the EMULATION target 
ARCH_EXE=$(OBJDIR)/$(DLNAME)
ARCH_BIN=$(OBJDIR)/lib$(DLNAME).so
ARCH_IMP=-l$(DLNAME)

#Emulation Mode. Load the libemu libraries
FMLIBS= \
	emufmemul \
	emufmsmfs \
	emufmcprov \
	emufmcsa8k \
	emufmserial \
	emufmciphobj

ARCH_LIBS= -Wl,--start-group $(LIBS:%=-l%) $(FMLIBS:%=-l%) -Wl,--end-group -ldl -lrt -lpthread

LFLAGS += -L$(FMLIBDIR)

ARCH_CLEAN= $(ARCH_OBJS) $(ARCH_EXE) $(ARCH_BIN)

CFLAGS+= -DFM_EMU -DEMUL -D_SFNT_FM_
CFLAGS += -std=c99 -D_FM_OBJTYPE_DLL_
LFLAGS += -L$(OBJDIR) -Wl,-zdefs -Wl,-rpath='$$ORIGIN' 
VPATH += $(OBJDIR)

else # end of FM
# specal code to build emulation version of HOST application
#
endif

else # end of EMUL

CONTINUE=1
endif

CFLAGS+= -DNOIDENT
CFLAGS+= -I. -I"$(FMDIR)/include" -I"$(CPROVDIR)/include" -I"$(CPROVDIR)/src/include"
CFLAGS+= -Wall -c

# define STOPONWARN to force compile to stop on warnings
ifneq ($(STOPONWARN), $(NULL))
CFLAGS+=-Werror
endif

ifneq ($(DEBUG),$(NULL))
# FM symbolic debugging is only available for EMUL builds
# if EMUL or Not FM and debug add symbolic debugging
ifneq ($(MODE),FM) 
    CFLAGS+= -g
endif
CFLAGS+=-DDEBUG
else
CFLAGS+=-O3
endif

# in case somebody declares a dependancy on ARCH_LIBS
.PHONY: --start-group --end-group -lgcc
--start-group:
--end-group:
-lgcc:

#enable continuation for emul or non-fm on linux
ifeq ($(CONTINUE),1)

# continue with unix specific definitions

# Detect the OS type
OSTYPE:=${shell OSTYPE=`uname -s`; case $$OSTYPE in [lL]inux*) OSTYPE=linux;; esac; echo $$OSTYPE }

############################################################################
#                                Linux/i386                                #
############################################################################
ifeq ($(OSTYPE),linux)

HWTYPE=${shell HWTYPE=`uname -m`; echo $$HWTYPE}
ifeq ($(HWTYPE),x86_64)
	override ARCH:=linux-x86_64
else
	override ARCH:=linux-i386
endif

LN:=$(CC)

CFLAGS += -D'tell(x)=lseek(x,0,SEEK_CUR)' -fPIC
CFLAGS += -Dushort_t='unsigned short'
CFLAGS += -Duint_t='unsigned int'
CFLAGS += -Dulong_t='unsigned long'
CFLAGS += -DHAS_STRSIGNAL -DNOIDENT -Wall
CFLAGS += -DIS_LITTLE_ENDIAN
SHARED:=-shared
STATIC:=-static
THREADS_LFLAG:=-lpthread
DL_LFLAG:=-ldl

ifeq ($(FM),)
ARCH_LIBS+=-lethsm
endif

endif # Linux
endif # FM && !EMUL (CONTINUE)

###########################################################################
#                         Some Generic Rules                              #
###########################################################################

ifeq ($(OBJDIR),$(NULL))
OBJDIR:=obj-$(ARCH)
endif

ifneq ($(EMUL),$(NULL))
OBJDIR:=$(OBJDIR)e
endif

ifneq ($(DEBUG),$(NULL))
OBJDIR:=$(OBJDIR)d
endif

ifneq ($(DLL),$(NULL))
LFLAGS+=$(SHARED)
ETHSMLFLAGS+=$(SHARED)
CPROVLFLAGS+=$(SHARED)
endif

VPATH+=$(CPROVLIBDIR) $(FMLIBDIR)

###########################################################################
#                    Some Generic Unix Build Rules                        #
###########################################################################
$(OBJDIR):
	@if [ ! -d $(OBJDIR) ] ; then $(MKDIR) $(OBJDIR) ; fi

$(OBJDIR)/%.o: %.c
	$(CC) $(CFLAGS) -c -o $@ $<

$(OBJDIR)/%.o: %.cpp
	$(CC) $(CFLAGS) -c -o $@ $<

$(OBJDIR)/%.o: %.s
	$(AS) $(AFLAGS) -o $@ $<

###########################################################################
#                    Power PC FM Build Rules (Linux hosted)               #
###########################################################################
ifeq ($(MODE),FM)     # if FM and not EMUL
# Special case to ignore the warning:
# 	"initialization discards qualifiers from pointer target type"
# which is caused by using a strings to populate char arrays
# in the macro DEFINE_FM_HEADER, which _should_ always be in hdr.c
# also force the header to be recompiled each time

$(OBJDIR)/hdr.o: hdr.c
	$(CC) $(CFLAGS) -Wno-error -c -o $@ $<

endif # powerpc

$(info OSTYPE=$(OSTYPE))
$(info ARCH=$(ARCH))
$(info OBJDIR=$(OBJDIR))
$(info CFLAGS=$(CFLAGS))
$(info FMDIR=$(FMDIR))
$(info CPROVDIR=$(CPROVDIR))
$(info FMLIBDIR=$(FMLIBDIR))
$(info CPROVLIBDIR=$(CPROVLIBDIR))

