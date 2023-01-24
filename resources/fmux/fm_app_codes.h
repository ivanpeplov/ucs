#define APP_BASE                                10000   // ������� �������� �����
#define APP_OK                                      0   // ��� ��
#define APP_KO                             APP_BASE+1   // ��� �����
#define APP_COMMAND_NOT_SUPPORT            APP_BASE+2   // ������ ������� �� �������������� ������ ����� FM.
                                                        // ���� ��� ���������� "��������".
#define APP_REQUEST_PARSE_ERROR            APP_BASE+3   // ������ ������� ������� �� ������� FM
#define APP_CRYPTOKI_ERROR                 APP_BASE+4   // ������ ��� ������ � ����������������� ����������� PSE
#define APP_OPEN_SERIAL_PORT_ERROR         APP_BASE+5   // ������ �������� ���-����� (��� ������)
#define APP_SET_SERIAL_PORT_ERROR          APP_BASE+6   // ������ ������������� ���-����� (������ � ����������)
#define APP_SET_SERIAL_ESTABLISHED_ERROR   APP_BASE+7   // ������ ������������ ���������� � �����������
#define APP_EXTDEV_SEND_TO_ERROR           APP_BASE+8   // ����-��� ��� �������� ������� � ���� (��������, ���� 
                                                        // �������� ���������� ��������� �� ����������) 
#define APP_EXTDEV_SEND_COMMAND_ERROR      APP_BASE+9   // ������ �������� ������
#define APP_EXTDEV_RECV_COMMAND_ERROR      APP_BASE+10  // ������ ��������� ������ �� ����������
#define APP_EXTDEV_PROTOCOL_ERROR          APP_BASE+11  // ������ ��������� (�������� ���������� ���� �� 
                                                        // ������������� ����������)

#define APP_GISKE_LEN_ERROR                APP_BASE+30  // ������ ����� �����-�����.

#define APP_CARDREADER_COMMUNICATION_ERROR APP_BASE+40  // ������ ��������� ����������
#define APP_CARDREADER_NO_CARD_ERROR       APP_BASE+41  // ����� �� ��������� � ���� �����
#define APP_CARDREADER_NO_POWER_ERROR      APP_BASE+42  // �� ����� ��� �������
#define APP_SA_ALREADY_INITIALIZED         APP_BASE+43  // ����� ��� ���� ������������������� (��������)
#define APP_SA_NOT_PRESENT                 APP_BASE+44  // ������ ����������� �� ����� ��� �������� � ��������
#define APP_WRITE_KEY_KCV_ERROR            APP_BASE+45  // KCV, ���������� � ������ ���������� �� KCV,

#define APP_AUTHENTICATION_ERROR           APP_BASE+60  // ������ ��������������!!! ����� 3-� ��������� ������� - ����������  
#define APP_FINDOBJECT_ERROR               APP_BASE+61  // ������ ������ ������������������ �������
#define APP_KBPK_FINDOBJECT_ERROR          APP_BASE+62  // ������ ������ KBPK ������������������ �������
#define APP_SESSION_FINDOBJECT_ERROR       APP_BASE+63  // ������ ������ ����������� ������������������ �������
//
#define APP_KEYBLOCK_ERROR                 APP_BASE+80  // ����� ������ KeyBlock
#define APP_KEYBLOCK_PARAM_ERROR           APP_BASE+81  // ������ ���������� ��� �������� KeyBlock
#define APP_KEYBLOCK_MO_PARAM_ERROR        APP_BASE+82  // ������ ������������ ���������� ��� �������� KeyBlock
#define APP_MEMORY_PARSE_ERROR             APP_BASE+83  // ������ ��������� ������ ��� �������� KeyBlock
#define APP_KEYBLOCK_LEN_ERROR             APP_BASE+84  // ������ ����� KeyBlock
#define APP_KEYBLOCK_ID_ERROR              APP_BASE+85  // ������ Version ID KeyBlock
#define APP_KEYBLOCK_PADDING_ERROR         APP_BASE+86  // ������ ������������ KeyBlock
#define APP_KEYBLOCK_HEADER_ERROR          APP_BASE+87  // ������ � ��������� KeyBlock
#define APP_KEYBLOCK_CHAR_HEADER_ERROR     APP_BASE+88  // ������ � ���������� ������� ��������� KeyBlock
#define APP_KEYBLOCK_OH_LEN_ERROR          APP_BASE+89  // ������ ������� ����� OHeader
#define APP_KEYBLOCK_OH_COUNT_ERROR        APP_BASE+90  // ����� �������������� ���������� � KeyBlock
#define APP_KEYBLOCK_OH_MCOUNT_ERROR       APP_BASE+91  // ����� �������������� ���������� PB � KeyBlock
#define APP_KEYBLOCK_MEM_ERROR             APP_BASE+92  // ��� ������ ��� ���������� � KeyBlock
#define APP_KEYBLOCK_OHEADER_ERROR         APP_BASE+93  // ������ � �������������� ��������� KeyBlock
#define APP_KEYBLOCK_ZMK_ERROR             APP_BASE+94  // ������ ������ � ZMK � KeyBlock
#define APP_KEYBLOCK_ZMK_CREATE_ERROR      APP_BASE+95  // ������ �������� ������ �� ZMK
#define APP_KEYBLOCK_MAC_ERROR             APP_BASE+96  // ������ MAC � KeyBlock
#define APP_KEYBLOCK_KEY_ERROR             APP_BASE+97  // ������ ��������� TMK �� KeyBlock
#define APP_KEYBLOCK_KBEK_NOT_FOUND        APP_BASE+98  // Define KBEK not found
#define APP_KEYBLOCK_CALCK_MAC_ERROR       APP_BASE+99  // Error Calck MAC for KeyBlock DesWork()
#define APP_KEYBLOCK_CREATE_ENCRYPT_PART   APP_BASE+100 // Error create ENCRYPT part
//                                                   
#define APP_KEY_IS_ALREADY_LOADED          APP_BASE+150 // ���� ��� �������� - ��� FmU1 - ��� �������������
                                                        // ��������� ����� ������
#define APP_COMMON_MD_INTERFACE_ERROR      APP_BASE+200 // ������������ ��� ������  // 200 // ������ MD ����������
