//
// EvhFetchCardCommand.h
// generated at 2016-04-05 13:45:26 
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

