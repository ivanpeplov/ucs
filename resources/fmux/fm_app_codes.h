#define APP_BASE                                10000   // Базовое смешение кодов
#define APP_OK                                      0   // Все ОК
#define APP_KO                             APP_BASE+1   // Все плохо
#define APP_COMMAND_NOT_SUPPORT            APP_BASE+2   // Данная команда не поддерживается данным типом FM.
                                                        // Этот код возвращают "заглушки".
#define APP_REQUEST_PARSE_ERROR            APP_BASE+3   // Ошибка разбора запроса на стороне FM
#define APP_CRYPTOKI_ERROR                 APP_BASE+4   // Ошибки при работе с криптографической библиотекой PSE
#define APP_OPEN_SERIAL_PORT_ERROR         APP_BASE+5   // Ошибка открытия ком-порта (уже открыт)
#define APP_SET_SERIAL_PORT_ERROR          APP_BASE+6   // Ошибка инициализации ком-порта (ошибка в параметрах)
#define APP_SET_SERIAL_ESTABLISHED_ERROR   APP_BASE+7   // Ошибка установления соединения с приложением
#define APP_EXTDEV_SEND_TO_ERROR           APP_BASE+8   // Тайм-аут при отправке команды в порт (например, если 
                                                        // конечное устройство физически не подключено) 
#define APP_EXTDEV_SEND_COMMAND_ERROR      APP_BASE+9   // Ошибки форматов команд
#define APP_EXTDEV_RECV_COMMAND_ERROR      APP_BASE+10  // Ошибка получения ответа от устройства
#define APP_EXTDEV_PROTOCOL_ERROR          APP_BASE+11  // Ошибка протокола (принятое количество байт не 
                                                        // соответствует ожидаемому)

#define APP_GISKE_LEN_ERROR                APP_BASE+30  // Ошибка длины гиске-блока.

#define APP_CARDREADER_COMMUNICATION_ERROR APP_BASE+40  // Ошибки протокола кардридера
#define APP_CARDREADER_NO_CARD_ERROR       APP_BASE+41  // Карта не вставлена в карт ридер
#define APP_CARDREADER_NO_POWER_ERROR      APP_BASE+42  // На карте нет питания
#define APP_SA_ALREADY_INITIALIZED         APP_BASE+43  // Карта уже была проинициализирована (заменить)
#define APP_SA_NOT_PRESENT                 APP_BASE+44  // Апплет отсутствует на карте или загружен с ошибками
#define APP_WRITE_KEY_KCV_ERROR            APP_BASE+45  // KCV, переданная с ключом отличается от KCV,

#define APP_AUTHENTICATION_ERROR           APP_BASE+60  // Ошибка аутентификации!!! После 3-х неудачных попыток - блокировка  
#define APP_FINDOBJECT_ERROR               APP_BASE+61  // Ошибка поиска криптографического объекта
#define APP_KBPK_FINDOBJECT_ERROR          APP_BASE+62  // Ошибка поиска KBPK криптографического объекта
#define APP_SESSION_FINDOBJECT_ERROR       APP_BASE+63  // Ошибка поиска сессионного криптографического объекта
//
#define APP_KEYBLOCK_ERROR                 APP_BASE+80  // Общая ошибка KeyBlock
#define APP_KEYBLOCK_PARAM_ERROR           APP_BASE+81  // Ошибка параметров для создания KeyBlock
#define APP_KEYBLOCK_MO_PARAM_ERROR        APP_BASE+82  // Ошибка обязательных параметров для создания KeyBlock
#define APP_MEMORY_PARSE_ERROR             APP_BASE+83  // Ошибка выделения памяти для создания KeyBlock
#define APP_KEYBLOCK_LEN_ERROR             APP_BASE+84  // Ошибка длины KeyBlock
#define APP_KEYBLOCK_ID_ERROR              APP_BASE+85  // Ошибка Version ID KeyBlock
#define APP_KEYBLOCK_PADDING_ERROR         APP_BASE+86  // Ошибка выравнивания KeyBlock
#define APP_KEYBLOCK_HEADER_ERROR          APP_BASE+87  // Ошибка в заголовке KeyBlock
#define APP_KEYBLOCK_CHAR_HEADER_ERROR     APP_BASE+88  // Ошибка в непечатном символе заголовка KeyBlock
#define APP_KEYBLOCK_OH_LEN_ERROR          APP_BASE+89  // Ошибка символа длины OHeader
#define APP_KEYBLOCK_OH_COUNT_ERROR        APP_BASE+90  // Много дополнительных заголовков в KeyBlock
#define APP_KEYBLOCK_OH_MCOUNT_ERROR       APP_BASE+91  // Много дополнительных заголовков PB в KeyBlock
#define APP_KEYBLOCK_MEM_ERROR             APP_BASE+92  // Нет памяти для сохранения в KeyBlock
#define APP_KEYBLOCK_OHEADER_ERROR         APP_BASE+93  // Ошибка в дополнительном заголовке KeyBlock
#define APP_KEYBLOCK_ZMK_ERROR             APP_BASE+94  // Ошибка работы с ZMK в KeyBlock
#define APP_KEYBLOCK_ZMK_CREATE_ERROR      APP_BASE+95  // Ошибка создания ключей из ZMK
#define APP_KEYBLOCK_MAC_ERROR             APP_BASE+96  // Ошибка MAC в KeyBlock
#define APP_KEYBLOCK_KEY_ERROR             APP_BASE+97  // Ошибка получения TMK из KeyBlock
#define APP_KEYBLOCK_KBEK_NOT_FOUND        APP_BASE+98  // Define KBEK not found
#define APP_KEYBLOCK_CALCK_MAC_ERROR       APP_BASE+99  // Error Calck MAC for KeyBlock DesWork()
#define APP_KEYBLOCK_CREATE_ENCRYPT_PART   APP_BASE+100 // Error create ENCRYPT part
//                                                   
#define APP_KEY_IS_ALREADY_LOADED          APP_BASE+150 // Ключ уже загружен - для FmU1 - для идентификации
                                                        // различных веток ключей
#define APP_COMMON_MD_INTERFACE_ERROR      APP_BASE+200 // Максимальный код ошибки  // 200 // Ошибка MD интерфейса
