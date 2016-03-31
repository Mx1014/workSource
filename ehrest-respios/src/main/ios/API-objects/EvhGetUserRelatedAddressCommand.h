//
// EvhGetUserRelatedAddressCommand.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserRelatedAddressCommand
//
@interface EvhGetUserRelatedAddressCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

