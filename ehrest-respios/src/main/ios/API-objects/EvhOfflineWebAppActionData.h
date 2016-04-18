//
// EvhOfflineWebAppActionData.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOfflineWebAppActionData
//
@interface EvhOfflineWebAppActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* realm;

@property(nonatomic, copy) NSString* entryUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

