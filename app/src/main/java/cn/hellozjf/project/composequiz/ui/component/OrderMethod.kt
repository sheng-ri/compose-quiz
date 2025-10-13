package cn.hellozjf.project.composequiz.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cn.hellozjf.project.composequiz.util.OrderMethodConstant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderMethod(
  dropdownExpand: Boolean,
  onDropdownExpandChange: (Boolean) -> Unit,
  orderMethods: List<String>,
  selectedOrderMethod: String,
  onSelectedOrderMethodChange: (String) -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically
  ) {
    Spacer(
      modifier = Modifier.size(16.dp)
    )
    Text("排序方式")
    Spacer(
      modifier = Modifier.size(16.dp)
    )
    CommonDropdown(
      expanded = dropdownExpand,
      onExpandedChange = onDropdownExpandChange,
      items = orderMethods,
      selectedItem = selectedOrderMethod,
      onSelectedItemChange = onSelectedOrderMethodChange
    )
  }
}


@Preview(
  showBackground = true
)
@Composable
fun OrderMethodPreview() {
  var dropdownMenuExpand by remember { mutableStateOf(false) }
  val onDropdownMenuExpandChange: (Boolean) -> Unit = {
    dropdownMenuExpand = it
  }
  val dropdownMenuItems: List<String> = listOf(
    OrderMethodConstant.CHAPTER,
    OrderMethodConstant.FAVORITE_TIME,
    OrderMethodConstant.WRONG_ANSWER_COUNT,
  )
  var selectOrderMethod by remember { mutableStateOf(OrderMethodConstant.CHAPTER) }
  val onSelectOrderMethodChange: (String) -> Unit = {
    selectOrderMethod = it
  }
  OrderMethod(
    dropdownExpand = dropdownMenuExpand,
    onDropdownExpandChange = onDropdownMenuExpandChange,
    orderMethods = dropdownMenuItems,
    selectedOrderMethod = selectOrderMethod,
    onSelectedOrderMethodChange = onSelectOrderMethodChange
  )
}
