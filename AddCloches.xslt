<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:preserve-space elements="o"/>
  <xsl:template match="@* | node()" name="identity">
    <xsl:copy>
      <xsl:apply-templates select="@* | node()"/>
    </xsl:copy>
  </xsl:template>

  <!--
   a: ImageSetID, c: InstallationPackageID, j: TransparencyMaskBitmapFilename,
   d: ImageWidthPixels, e: ImageHeightPixels, g: ClickableAreaRightRelativeXPosPixels,
   i: ClickableAreaBottomRelativeYPosPixels
    -->
  <xsl:template match="ObjectList[@ObjectType='ImageSet']">
    <xsl:copy>
      <xsl:copy-of select="@*|o"/>
      <xsl:text>&#10;</xsl:text>
      <o><a>20000</a><c>138</c><b>GO Cloches</b><d>40</d><e>40</e><g>40</g><i>40</i></o>
      <xsl:text>&#10;</xsl:text>
    </xsl:copy>
  </xsl:template>

  <!--
   a: ImageSetID, b: ImageIndexWithinSet (defaults to 1)
    -->
  <xsl:template match="ObjectList[@ObjectType='ImageSetElement']">
    <xsl:copy>
      <xsl:copy-of select="@*|o"/>
      <xsl:text>&#10;</xsl:text>
      <o><a>20000</a><c>GO Cloches Off</c><d>Metz-Cavaille-Coll-Images/go-cloches-off.bmp</d></o>
      <o><a>20000</a><c>GO Cloches On</c><d>Metz-Cavaille-Coll-Images/go-cloches-on.bmp</d><b>2</b></o>
      <xsl:text>&#10;</xsl:text>
    </xsl:copy>
  </xsl:template>

  <!--
   a: ImageSetInstanceID, c: ImageSetID, e: DisplayPageID,
   f: ScreenLayerNumber (1=console, 2=full view)
   g: LeftXPosPixels, h: TopYPosPixels
    -->
  <xsl:template match="ObjectList[@ObjectType='ImageSetInstance']">
    <xsl:copy>
      <xsl:copy-of select="@*|o"/>
      <xsl:text>&#10;</xsl:text>
      <o><a>20000</a><c>20000</c><e>2</e><b>GO Cloches</b><g>448</g><h>422</h></o>
      <xsl:text>&#10;</xsl:text>
    </xsl:copy>
  </xsl:template>

  <!--
    a: SwitchID, c: DefaultInputOutputSwitchAsgnCode, k: Disp_ImageSetInstanceID, i: Latching,
    l: Disp_ImageSetIndexEngaged, m: Disp_ImageSetIndexDisengaged
    <c>2400</c> < - - - - not needed?
    -->
  <xsl:template match="ObjectList[@ObjectType='Switch']">
    <xsl:copy>
      <xsl:copy-of select="@*|o"/>
      <xsl:text>&#10;</xsl:text>
      <o><a>20000</a><k>20000</k><i>Y</i><b>99-GO-Cloches</b><l>2</l><m>1</m></o>
      <xsl:text>&#10;</xsl:text>
    </xsl:copy>
  </xsl:template>

  <!--
    a: SourceSwitchID, b: DestSwitchID, f: EngageLinkActionCode, g: DisengageLinkActionCode
    This uses the stop noise for the GO Flute 4.
    -->
  <xsl:template match="ObjectList[@ObjectType='SwitchLinkage']">
    <xsl:copy>
      <xsl:copy-of select="@*|o"/>
      <xsl:text>&#10;</xsl:text>
      <o><a>20000</a><b>100043</b><f>4</f><g>7</g></o>
      <o><a>20000</a><b>100026</b><f>7</f><g>4</g></o>
      <xsl:text>&#10;</xsl:text>
    </xsl:copy>
  </xsl:template>

  <!--
   a: ExternalRankID, b: Name, c: PipeGen_GeneratePipesAutomatically,
   d: PipeGen_MIDINoteNumOfFirstPipe (57=tenor A, 36=C), e: PipeGen_NumberOfPipes
   -->
  <xsl:template match="ObjectList[@ObjectType='ExternalRank']">
    <xsl:copy>
      <xsl:copy-of select="@*|o"/>
      <xsl:text>&#10;</xsl:text>
      <o><a>10001</a><b>Cloches</b><c>Y</c><d>36</d><e>56</e></o>
      <xsl:text>&#10;</xsl:text>
    </xsl:copy>
  </xsl:template>

  <!--
   a: StopID, c: DivisionID, d: ControllingSwitchID
  -->
  <xsl:template match="ObjectList[@ObjectType='Stop']">
    <xsl:copy>
      <xsl:copy-of select="@*|o"/>
      <xsl:text>&#10;</xsl:text>
      <o><a>10000</a><c>2</c><d>20000</d><b>Cloches</b></o>
      <xsl:text>&#10;</xsl:text>
    </xsl:copy>
  </xsl:template>


  <!--
   a: StopID, b: Name, c: RankTypeCode, e: ExternalRankId,
   f: ActionTypeCode, g: ActionEffectCode, i: NumberOfMappedDivisionInputNodes
   -->
  <xsl:template match="ObjectList[@ObjectType='StopRank']">
    <xsl:copy>
      <xsl:copy-of select="@*|o"/>
      <xsl:text>&#10;</xsl:text>
      <o><a>10000</a><b>Cloches</b><c>2</c><e>10001</e><f>1</f><g>1</g><i>56</i></o>
      <xsl:text>&#10;</xsl:text>
    </xsl:copy>
  </xsl:template>


  <!--
   a: CombinationElementID, b: CombinationID, c: ControlledSwitchID, d: CapturedSwitchID
  -->
  <xsl:template match="ObjectList[@ObjectType='CombinationElement']">
    <xsl:copy>
      <xsl:copy-of select="@*|o"/>
      <xsl:text>&#10;</xsl:text>
      <o><a>1419</a><b>11</b><c>20000</c><d>20000</d></o>
      <o><a>1420</a><b>12</b><c>20000</c><d>20000</d></o>
      <o><a>1421</a><b>13</b><c>20000</c><d>20000</d></o>
      <o><a>1422</a><b>14</b><c>20000</c><d>20000</d></o>
      <o><a>1423</a><b>15</b><c>20000</c><d>20000</d></o>
      <o><a>1424</a><b>16</b><c>20000</c><d>20000</d></o>
      <o><a>1425</a><b>17</b><c>20000</c><d>20000</d></o>
      <o><a>1426</a><b>18</b><c>20000</c><d>20000</d></o>
      <o><a>1427</a><b>19</b><c>20000</c><d>20000</d></o>
      <o><a>1428</a><b>20</b><c>20000</c><d>20000</d></o>
      <o><a>1429</a><b>21</b><c>20000</c><d>20000</d></o>
      <o><a>1430</a><b>22</b><c>20000</c><d>20000</d></o>
      <o><a>1431</a><b>23</b><c>20000</c><d>20000</d></o>
      <o><a>1432</a><b>24</b><c>20000</c><d>20000</d></o>
      <o><a>1433</a><b>25</b><c>20000</c><d>20000</d></o>
      <o><a>1434</a><b>26</b><c>20000</c><d>20000</d></o>
      <o><a>1435</a><b>27</b><c>20000</c><d>20000</d></o>
      <o><a>1436</a><b>28</b><c>20000</c><d>20000</d></o>
      <o><a>1437</a><b>29</b><c>20000</c><d>20000</d></o>
      <o><a>1438</a><b>30</b><c>20000</c><d>20000</d></o>
      <o><a>1439</a><b>1</b><c>20000</c><d>20000</d></o>
      <o><a>1440</a><b>999998</b><c>20000</c></o>
      <xsl:text>&#10;</xsl:text>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
