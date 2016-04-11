//
// EvhListGroupCommandResponse.h
// generated at 2016-04-07 17:57:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhGroupDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListGroupCommandResponse
//
@interface EvhListGroupCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhGroupDTO*
@property(nonatomic, strong) NSMutableArray* groups;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

