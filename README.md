# CircularLayoutManager

## Setup Instructions

**Gradle**
  ``` gradle
        implementation 'com.darklabs:circularLayoutManager:0.0.1.2'
  ```

**Maven**
  ``` maven
        <dependency>
        <groupId>com.darklabs</groupId>
        <artifactId>circularLayoutManager</artifactId>
        <version>0.0.1.2</version>
        <type>pom</type>
        </dependency>
  ```

  ## Attributes
  <table>
  <th>Attribute</th>
  <th>Format</th>
  <th>Description</th>
  <tr>
  <td>orientation</td>
  <td>horizontal/vertical</td>
  <td>`horizontal` to set the layout to flow horizontal alignment, `vertical` to follow
  vertical. Default is `vertical`</td>
  </tr>
  <tr>
  <td>gravity</td>
  <td>start/end</td>`
  <td>`start` to position center of circle at start of layout or `end` to set center at the end
  of layout. Default is `start`</td>
  </tr>
  <tr>
  <td>rotating</td>
  <td>boolean</td>
  <td>`true` to orbit the layout around layout or `false` to maintain their angle wrt screen.
  Default is `false`</td>
  </tr>
  <tr>
  <td>app:ticketScallopPositionPercent="50"</td>
  <td>50</td>
  <td>sets position of scallop and divider</td>
  </tr>
  <tr>
  <td>peekDistance</td>
  <td>integer</td>
  <td>The distance at which layout will start getting placed. Default is 500</td>
  </tr>
  <tr>
  <td>app:ticketBorderWidth="4dp"</td>
  <td>2dp</td>
  <td>sets border width</td>
  </tr>
  <tr>
  <td>radius</td>
  <td>integer</td>
  <td>The curvature of the arc. Default is 2000</td>
  </tr>
  <tr>
  <td>scalingFactor</td>
  <td>`none`/`half`/`full`</td>
  <td>The factor at which to scale items. Default is `none`</td>
  </tr>
  <tr>
  <td>scaling</td>
  <td>boolean</td>
  <td>Whether to scale items or not. Default is `false`</td>
  </tr>
  </table>




  ## License

  ```
  © DarkLabs, 2020

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

  Author : Rooparsh Kalia
  ```
