<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:android="http://schemas.android.com/apk/res/android">

  <xsl:param name="gameanalyticsID"/>
  <xsl:param name="gameanalyticsSecretKey"/>
  <xsl:template match="meta-data[@android:name='gameanalyticsID']">
    <meta-data android:name="gameanalyticsID" android:value="{$gameanalyticsID}"/>
  </xsl:template>
  <xsl:template match="meta-data[@android:name='gameanalyticsSecretKey']">
    <meta-data android:name="gameanalyticsSecretKey" android:value="{$gameanalyticsSecretKey}"/>
  </xsl:template>

  <xsl:output indent="yes" />
  <xsl:template match="comment()" />
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
