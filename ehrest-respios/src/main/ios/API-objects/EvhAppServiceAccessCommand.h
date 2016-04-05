//
// EvhAppServiceAccessCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppServiceAccessCommand
//
@interface EvhAppServiceAccessCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* appKey;

@property(nonatomic, copy) NSString* uri;

@property(nonatomic, copy) NSString* type;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

