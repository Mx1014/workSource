//
// EvhSendUserTestMailCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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

