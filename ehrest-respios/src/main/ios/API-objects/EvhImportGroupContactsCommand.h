//
// EvhImportGroupContactsCommand.h
// generated at 2016-03-31 13:49:12 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhImportGroupContactsCommand
//
@interface EvhImportGroupContactsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

