package cn.hellozjf.project.composequiz

import org.apache.poi.ss.usermodel.CellStyle
import org.apache.poi.ss.usermodel.Font
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class ExcelTest {

  fun writeToExcel(
    headers: List<String>,
    data: List<List<String>>
  ) {
    // 创建工作簿
    val workbook: Workbook = XSSFWorkbook()
    val sheet: Sheet = workbook.createSheet("数据表")

    // 创建标题行
    val headerRow: Row = sheet.createRow(0)

    headers.forEachIndexed { index, header ->
      val cell = headerRow.createCell(index)
      cell.setCellValue(header)

      // 设置标题样式
      val style: CellStyle = workbook.createCellStyle()
      val font: Font = workbook.createFont()
      font.bold = true
      style.setFont(font)
      cell.cellStyle = style
    }

    // 创建数据行
    data.forEachIndexed { rowIndex, rowData ->
      val row: Row = sheet.createRow(rowIndex + 1)
      rowData.forEachIndexed { cellIndex, cellData ->
        val cell = row.createCell(cellIndex)
        cell.setCellValue(cellData)
      }
    }

    // 自动调整列宽
    headers.indices.forEach { index ->
      sheet.autoSizeColumn(index)
    }

    // 写入文件
    FileOutputStream("output.xlsx").use { outputStream ->
      workbook.write(outputStream)
    }

    workbook.close()
    println("Excel 文件已生成")
  }
}