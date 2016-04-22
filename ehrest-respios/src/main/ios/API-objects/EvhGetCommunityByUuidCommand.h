//
// EvhGetCommunityByUuidCommand.h
// generated at 2016-04-22 13:56:48 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCommunityByUuidCommand
//
@interface EvhGetCommunityByUuidCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* uuid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

