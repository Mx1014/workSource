//
// EvhGetLaunchPadItemsByOrgCommand.h
// generated at 2016-04-19 12:41:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadItemsByOrgCommand
//
@interface EvhGetLaunchPadItemsByOrgCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* itemLocation;

@property(nonatomic, copy) NSString* itemGroup;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* sceneType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

