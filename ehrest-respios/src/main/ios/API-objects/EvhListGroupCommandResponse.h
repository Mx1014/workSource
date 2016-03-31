//
// EvhListGroupCommandResponse.h
// generated at 2016-03-31 13:49:14 
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

