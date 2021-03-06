----------------------------
条件式について
----------------------------

条件式には複数の種類があり、書式を拡張するために使用します。

* 書式は、大括弧"[", "]"で囲みます。
* 比較演算子（[>80]）
* 色（[色名]）
* ロケール（[$-<ロケールコード>]）(例. [$-403])
* 記号付きロケール（[$<記号>-<ロケールコード>]） (例. [$€-407])
* 特殊な数値表示（[DBNum<値>]）


^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
条件式（比較演算子）について
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

セクションごとに一致させる条件を比較演算子で指定することができます。

.. list-table:: 比較演算子の種類
   :widths: 15 20 20 15 30
   :header-rows: 1
   
   
   * - 演算子
     - 名称
     - 名称（英字）
     - 使用例
     - 結果
   
   * - =
     - 等しい 
     - equal
     - [=2]
     - 2と等しい。
     
   * - <>
     - 等しくない
     - not equal
     - [<>2]
     - 2以外。
     
   * - >
     - 大きい
     - greater than
     - [>2]
     - 2より大きい。
     
   * - <
     - 小さい
     - less than
     - [<2]
     - 2より小さい。
     
   * - >=
     - 以上
     - greater equal
     - [>=2]
     - 2以上。
     
   * - <=
     - 以下
     - less equal
     - [<=2]
     - 2以下。

^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
色の条件について
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

色は名前付きの8色の他、番号で指定可能で、全56色を指定できます。

Excel2003までは、Excelでの色のパレットは56色だったことに由来します。


.. list-table:: 色の書式記号
   :widths: 20 40 15 25
   :header-rows: 1
   
   
   * - 書式記号
     - 説明
     - 使用例
     - 表示例
   
   * - [<色名>]
     - | 色の名前（8色）を指定します。
       | OSなどの言語環境によって色名の指定方法が変わります。日本語環境の場合は、日本語名で指定します。
       | 指定可能な色の名前は、下記を参照してください。
     - | [青]##,###.0#
       | [赤]hh:mm a/p
     - | 12345.12 -> 1,2345.12
       | 11:45:34 -> 11:45 a

   * - [色<番号>]
     - | 色の番号（1から56番）を指定します。
       | 色の見本については、外部サイトの `Color Palette and the 56 Excel ColorIndex Colors <http://dmcritchie.mvps.org/excel/colors.htm>`_ を参照してください。
     - | [色10]##,###.0#
       | [色7]hh:mm a/p
     - | 12345.12 -> 1,2345.12
       | 11:45:34 -> 11:45 a



.. list-table:: 名前付きの色
   :widths: 30 20 20 30
   :header-rows: 1
   
   * - 色名（英語）
     - 色名（日本語）
     - 色番号
     - RGB
     
   * - Black
     - 黒
     - 1
     - #000000
   
   * - White
     - 白
     - 2
     - #FFFFFF
     
   * - Red
     - 赤
     - 3
     - #FF0000
     
   * - Green
     - 緑
     - 4
     - #00FF00
     
   * - Blue
     - 青
     - 5
     - #0000FF
     
   * - Yellow
     - 黄
     - 6
     - #FFFF00
     
   * - Magenta
     - 紫
     - 7
     - #FF00FF

   * - Cyan
     - 水
     - 8
     - #00FFFF


.. note:: Excel内部における色の保持の仕方
   
   日本語環境のExcelでは色を書式上で指定する際には、日本語の色名で指定しますが、内部的には英字名で保持しています。
   
   そのため、POIやJExcelAPIなどのライブラリで書式を取得すると英字名で取得されます。


.. note:: 色で違いを表現する場合の注意事項
   
   色で違いを表現する場合は、色による違いだけなので、文字列としては区別が付かなくなり、プログラムで処理するようなときは面倒です。
   
   例えば、数値用の書式で正と負の書式を黒か赤で表現する「\0_);[Red]\\(0\\)」は、印刷したときや文字列として取得した場合、色による違いだけなので区別がつかなくなります。
   
   また、予め用意されている名前付きの色は輝度が高く、白黒で印刷すると薄く表示されるためあまりお薦めしません。比較的輝度が低い「青」「赤」を使用する程度にした方がよいと思います。


^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
ロケールの条件について
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

ロケールを書式に指定すると、その言語専用の表記に変換され表示されます。

Miscrosoftでは、ロケールごとにIDを振り、 *ロケールID（LCID）* と呼ばれています。

.. list-table:: ロケールの書式記号
   :widths: 20 40 15 25
   :header-rows: 1
   
   
   * - 書式記号
     - 説明
     - 使用例
     - 表示例
   
   * - [$-<LCID>]
     - | LCIDは、16進数の値を指定します。
       | LCIDは、外部サイトの `ロケール ID (LCID) の一覧 <https://msdn.microsoft.com/ja-jp/library/cc392381.aspx>`_ を参照してください。
     - | [$-411]dddd
       | [$-409]dddd
     - | 2001/1/3 -> 水曜日
       | 2001/1/3 -> Wednesday


.. list-table:: よく使用するロケール
   :widths: 40 20 20 20
   :header-rows: 1
   
   * - ロケール名
     - 言語コード
     - 16進数
     - 10進数
     
   * - 英語 (U.K.)
     - en-gb
     - 0x809
     - 2057
     
   * - 英語 (U.S.)
     - en-us
     - 0x409
     - 1033
     
   * - 日本語
     - ja
     - 0x411
     - 1041
     


.. note:: 特別なLCID
   
   Excelの中で予め用意されている書式の中で、LCIDの一覧にない *[$-F800]* と *[$-F400]* があります。
   これらは、システムの日付、時刻の書式に指定されて、OSの言語によって変わる書式です。
   
   例えば、日付用の書式「[$-F800]dddd\\,\\ mmmm\\ dd\\,\\ yyyy」は、日本語環境では「yyyy"年"m"月"d"日"」として処理されます。
   さらに、時間用の書式「[$-F400]h:mm:ss\\ AM/PM」は、日本語環境では「h:mm:ss」として処理されます。


^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
記号付きロケールの条件について
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

会計の書式において、単位の記号（ ``￥, $`` ）は、通常は先頭に付く。

しかし、ドイツマルクの記号（ ``€`` ）など一部、最後尾に付く場合がある。
そのような場合、特殊なロケールの条件式が使われる。

形式は、 ``[$<記号>-<ロケールコード(LCID)>]`` (例. [$€-407]) であるが、記述した位置に、``<記号>`` が出力される。


^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
言語固有の数値の表現について
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

全角の数字など言語固有の数値の表現を指定することができます。

これは、 **Excel2002以降（Excel XP）でのみ利用可能な機能** であり、Excel2000以前ではサポートされません。

.. list-table:: 日本語環境で利用可能な数値表示
   :widths: 10 10 20 30 30
   :header-rows: 1
   
   * - 書式記号
     - 言語
     - 説明
     - 使用例
     - 表示例
     
   * - [DBNum1]
     - 日本語
     - 数字を漢数字で表示します。
     - | [DBNum1][$-411]G/標準
       | [DBNum1][$-411]ggge"年"m"月"d"日"
     - | 12345 -> 一万二千三百四十五
       | 1959/12/10 -> 昭和三十四年十二月十日
       
   * - [DBNum2]
     - 日本語
     - 数字を大字（だいじ）で表示します。
     - | [DBNum2][$-411]G/標準
       | [DBNum2][$-411]ggge"年"m"月"d"日"
     - | 12345 -> 壱萬弐阡参百四拾伍
       | 1959/12/10 -> 昭和参拾四年壱拾弐月壱拾日

   * - [DBNum3]
     - 日本語
     - 数字を全角で表示します。
     - | [DBNum3][$-411]G/標準
       | [DBNum3][$-411]ggge"年"m"月"d"日"
     - | 12345 -> １２３４５
       | 1959/12/10 -> 昭和３４年１２月１０日



.. note:: 言語固有の数値の表示条件の注意事項
   
   数値の表示表現をする *[DBNumN]* は、言語ごとに意味が異なるため、他の言語環境でExcelファイルを開いたときを考慮して、
   通常はロケールの条件も一緒に定義します。
   
   他の言語で利用可能な数値表現は、外部サイトの `Libre Officeの数の書式コード <https://help.libreoffice.org/Common/Number_Format_Codes/ja>`_ 
   が参考になると思います。
   

