//
// EvhYellowPageListResponse.h
// generated at 2016-03-31 10:18:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhYellowPageDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhYellowPageListResponse
//
@interface EvhYellowPageListResponse
    : NSObject<EvhJsonSerializable>


// item type EvhYellowPageDTO*
@property(nonatomic, strong) NSMutableArray* yellowPages;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

