//
// EvhFetchCardCommand.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFetchCardCommand
//
@interface EvhFetchCardCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* applierPhone;

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

