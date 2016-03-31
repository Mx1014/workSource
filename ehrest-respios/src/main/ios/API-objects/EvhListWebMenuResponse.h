//
// EvhListWebMenuResponse.h
// generated at 2016-03-31 19:08:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhWebMenuDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWebMenuResponse
//
@interface EvhListWebMenuResponse
    : NSObject<EvhJsonSerializable>


// item type EvhWebMenuDTO*
@property(nonatomic, strong) NSMutableArray* menus;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

