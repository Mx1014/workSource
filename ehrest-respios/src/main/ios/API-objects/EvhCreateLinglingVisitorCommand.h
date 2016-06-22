//
// EvhCreateLinglingVisitorCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateLinglingVisitorCommand
//
@interface EvhCreateLinglingVisitorCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* visitorEvent;

@property(nonatomic, copy) NSString* organization;

@property(nonatomic, copy) NSString* description_;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

