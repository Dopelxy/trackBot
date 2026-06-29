package com.dopelxy.trackbot.integration.russianpost

import com.dopelxy.trackbot.model.TrackingEvent
import org.w3c.dom.Document
import org.w3c.dom.Node
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

class XmlParser {

    fun parse(file: File): List<TrackingEvent> {

        val builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document: Document = builder.parse(file)

        document.documentElement.normalize()

        val historyRecords =
            document.getElementsByTagName("ns3:historyRecord")

        val events = mutableListOf<TrackingEvent>()

        for (i in 0 until historyRecords.length) {

            val record = historyRecords.item(i)

            val operationParameters =
                findChild(record, "ns3:OperationParameters")

            val operType =
                findChild(operationParameters, "ns3:OperType")

            val operAttr =
                findChild(operationParameters, "ns3:OperAttr")

            val addressParameters =
                findChild(record, "ns3:AddressParameters")

            val operationAddress =
                findChild(addressParameters, "ns3:OperationAddress")

            val date = java.time.OffsetDateTime.parse(
                getChildText(operationParameters, "ns3:OperDate")
            ).toLocalDateTime()

            events.add(

                TrackingEvent(

                    date = date,

                    operation = getChildText(
                        operType,
                        "ns3:Name"
                    ),

                    attribute = getChildText(
                        operAttr,
                        "ns3:Name"
                    ),

                    location = getChildText(
                        operationAddress,
                        "ns3:Description"
                    )
                )
            )
        }

        return events
    }

    private fun findChild(
        node: Node?,
        tagName: String
    ): Node? {

        if (node == null) return null

        val children = node.childNodes

        for (i in 0 until children.length) {

            val child = children.item(i)

            if (child.nodeName == tagName) {
                return child
            }
        }

        return null
    }

    private fun getNodeText(node: Node?): String {

        return node?.textContent?.trim().orEmpty()
    }

    private fun getChildText(
        node: Node?,
        childName: String
    ): String {

        return getNodeText(
            findChild(node, childName)
        )
    }
}