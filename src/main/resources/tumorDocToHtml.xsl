<!--
XSLT to render HTML from TumorML 1.1 documents
Author: David Johnson
2013-, TumorML contributors.
This stylesheet is distributed under the terms of the GNU Lesser General Public License (LGPL)
http://www.gnu.org/licenses/lgpl.html
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" indent="yes" />
    <xsl:template match="/">
        <article>
            <xsl:apply-templates select="tumorml/header" />
            <xsl:apply-templates select="tumorml/model" />
        </article>
    </xsl:template>

    <xsl:template match="header">
        <h1>Title: <xsl:value-of select="title" /></h1>
        <p>Creator: <xsl:value-of select="creator/person/fullname" /></p>
        <p>Publisher: <xsl:value-of select="publisher/person/fullname" /></p>
        <p>Date published: <xsl:value-of select="date" /></p>
        <p>Math type: <xsl:value-of select="math" /></p>
        <p>Biocomplexity direction: <xsl:value-of select="biocomplexityDirection" /></p>
        <p>Cancer simulated: <xsl:value-of select="cancer" /></p>
        <p>Materialization: <xsl:value-of select="materializtion" /></p>
        <p>Tumor homogeneity: <xsl:value-of select="homogeneity" /></p>
        <p>Image-based detectability: <xsl:value-of select="imageBasedDetectability" /></p>
        <p>Free growth simulated?: <xsl:value-of select="freeGrowth" /></p>
        <p>Treatment included: <xsl:value-of select="treatmentIncluded" /></p>
    </xsl:template>

    <xsl:template match="model">
        <h2>Model</h2>
        <xsl:apply-templates select="parameters"/>
    </xsl:template>

    <xsl:template match="parameters">
        <table>
            <tr><td>Parameter</td><td>Identifier</td><td>Type</td><td>Optional?</td></tr>
            <xsl:for-each select="in">
                <tr><td>Input</td><td><xsl:value-of select="./@name" /></td><td><xsl:value-of select="value/@type" /></td><td><xsl:value-of select="./@optional" /></td></tr>
            </xsl:for-each>
            <xsl:for-each select="out">
                <tr><td>Output</td><td><xsl:value-of select="./@name" /></td><td><xsl:value-of select="value/@type" /></td><td><xsl:value-of select="./@optional" /></td></tr>
            </xsl:for-each>
        </table>
    </xsl:template>

</xsl:stylesheet>

