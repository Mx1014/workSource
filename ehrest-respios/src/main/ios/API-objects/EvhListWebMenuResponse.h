//
// EvhListWebMenuResponse.h
// generated at 2016-04-19 13:40:01 
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

