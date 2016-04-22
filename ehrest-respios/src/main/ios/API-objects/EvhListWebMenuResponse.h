//
// EvhListWebMenuResponse.h
// generated at 2016-04-22 13:56:46 
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

