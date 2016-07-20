//
// EvhSendUserTestMailCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendUserTestMailCommand
//
@interface EvhSendUserTestMailCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* from;

@property(nonatomic, copy) NSString* to;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* body;

@property(nonatomic, copy) NSString* attachment1;

@property(nonatomic, copy) NSString* attachment2;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

